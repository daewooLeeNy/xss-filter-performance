package com.example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.regex.Pattern;

public final class CustomXSSRequestWrapper extends HttpServletRequestWrapper {

    private static Pattern[] patterns = new Pattern[]{
            // Script fragments
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            // src='...'
            Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // lonely script tags
            Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<(.*?)script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // eval(...)
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // expression(...)
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // javascript:...
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            // vbscript:...
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            // onload(...)=...
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            Pattern.compile("alert\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("confirm\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("console.log\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("console\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),

            // additional pattern
            Pattern.compile("#exec", Pattern.CASE_INSENSITIVE),
            Pattern.compile("%0da=eval", Pattern.CASE_INSENSITIVE),
            Pattern.compile("%3Cscript", Pattern.CASE_INSENSITIVE),
            Pattern.compile("%3Ealert", Pattern.CASE_INSENSITIVE),
            Pattern.compile("%u0", Pattern.CASE_INSENSITIVE),
            Pattern.compile("&#x", Pattern.CASE_INSENSITIVE),
            Pattern.compile(";//", Pattern.CASE_INSENSITIVE),
            Pattern.compile("@import", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\";", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\+AD4", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\+ADw", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<br size=", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<htmlxmln", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("0%0d%0a%00", Pattern.CASE_INSENSITIVE),
            Pattern.compile("activexobject", Pattern.CASE_INSENSITIVE),
            Pattern.compile("acunetix_wvs", Pattern.CASE_INSENSITIVE),
            Pattern.compile("aim:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("alert", Pattern.CASE_INSENSITIVE),
            Pattern.compile("allowscriptaccess", Pattern.CASE_INSENSITIVE),
            Pattern.compile("append", Pattern.CASE_INSENSITIVE),
            Pattern.compile("applet", Pattern.CASE_INSENSITIVE),
            Pattern.compile("application/npruntime-scriptable-plugin", Pattern.CASE_INSENSITIVE),
            Pattern.compile("background", Pattern.CASE_INSENSITIVE),
            Pattern.compile("behavior", Pattern.CASE_INSENSITIVE),
            Pattern.compile("bgsound", Pattern.CASE_INSENSITIVE),
            Pattern.compile("binding", Pattern.CASE_INSENSITIVE),
            Pattern.compile("blink", Pattern.CASE_INSENSITIVE),
            Pattern.compile("charset", Pattern.CASE_INSENSITIVE),
            Pattern.compile("clsid:cafeefac-dec7-0000-0000-abcdeffedcba", Pattern.CASE_INSENSITIVE),
            Pattern.compile("cookie", Pattern.CASE_INSENSITIVE),
            Pattern.compile("create", Pattern.CASE_INSENSITIVE),
            Pattern.compile("deploymenttoolkit", Pattern.CASE_INSENSITIVE),
            Pattern.compile("document", Pattern.CASE_INSENSITIVE),
            Pattern.compile("dynsrc", Pattern.CASE_INSENSITIVE),
            Pattern.compile("echo\\(", Pattern.CASE_INSENSITIVE),
            Pattern.compile("embed", Pattern.CASE_INSENSITIVE),
            Pattern.compile("eval", Pattern.CASE_INSENSITIVE),
            Pattern.compile("expression", Pattern.CASE_INSENSITIVE),
            Pattern.compile("firefoxurl", Pattern.CASE_INSENSITIVE),
            Pattern.compile("frame", Pattern.CASE_INSENSITIVE),
            Pattern.compile("frameset", Pattern.CASE_INSENSITIVE),
            Pattern.compile("fromcharcode", Pattern.CASE_INSENSITIVE),
            Pattern.compile("FSCommand", Pattern.CASE_INSENSITIVE),
            Pattern.compile("getRuntime", Pattern.CASE_INSENSITIVE),
            Pattern.compile("href", Pattern.CASE_INSENSITIVE),
            Pattern.compile("http-equiv=\"refresh\"", Pattern.CASE_INSENSITIVE),
            Pattern.compile("http-equiv=refresh", Pattern.CASE_INSENSITIVE),
            Pattern.compile("iframe", Pattern.CASE_INSENSITIVE),
            Pattern.compile("ilayer", Pattern.CASE_INSENSITIVE),
            Pattern.compile("innerHTML", Pattern.CASE_INSENSITIVE),
            Pattern.compile("ja%0Av%0Aa%0As%0Ac%0Aript", Pattern.CASE_INSENSITIVE),
            Pattern.compile("java.lang.Runtime  ", Pattern.CASE_INSENSITIVE),
            Pattern.compile("JaVaScRiPt", Pattern.CASE_INSENSITIVE),
            Pattern.compile("javascript", Pattern.CASE_INSENSITIVE),
            Pattern.compile("layer", Pattern.CASE_INSENSITIVE),
            Pattern.compile("link", Pattern.CASE_INSENSITIVE),
            Pattern.compile("list-style-image", Pattern.CASE_INSENSITIVE),
            Pattern.compile("lowsrc", Pattern.CASE_INSENSITIVE),
            Pattern.compile("meta", Pattern.CASE_INSENSITIVE),
            Pattern.compile("microsoft.xmlhttp", Pattern.CASE_INSENSITIVE),
            Pattern.compile("moz-binding", Pattern.CASE_INSENSITIVE),
            Pattern.compile("msgbox", Pattern.CASE_INSENSITIVE),
            Pattern.compile("nabort", Pattern.CASE_INSENSITIVE),
            Pattern.compile("object", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onAbort", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onactivae", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onActivate", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onafterprint", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onafterupdate", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onbefore", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onbeforeactivate", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onbeforecopy", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onbeforecut", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onbeforedeactivate", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onbeforeeditfocus", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onbeforepaste", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onbeforeprint", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onbeforeunload", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onbeforeupdate", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onBegin", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onblur", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onbounce", Pattern.CASE_INSENSITIVE),
            Pattern.compile("oncellchange", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onchange", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onclick", Pattern.CASE_INSENSITIVE),
            Pattern.compile("oncontextmenu", Pattern.CASE_INSENSITIVE),
            Pattern.compile("oncontrolselect", Pattern.CASE_INSENSITIVE),
            Pattern.compile("oncopy", Pattern.CASE_INSENSITIVE),
            Pattern.compile("oncut", Pattern.CASE_INSENSITIVE),
            Pattern.compile("ondataavailable", Pattern.CASE_INSENSITIVE),
            Pattern.compile("ondatasetchanged", Pattern.CASE_INSENSITIVE),
            Pattern.compile("ondatasetcomplete", Pattern.CASE_INSENSITIVE),
            Pattern.compile("ondblclick", Pattern.CASE_INSENSITIVE),
            Pattern.compile("ondeactivate", Pattern.CASE_INSENSITIVE),
            Pattern.compile("ondrag", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onDragDrop", Pattern.CASE_INSENSITIVE),
            Pattern.compile("ondragend", Pattern.CASE_INSENSITIVE),
            Pattern.compile("ondragenter", Pattern.CASE_INSENSITIVE),
            Pattern.compile("ondragleave", Pattern.CASE_INSENSITIVE),
            Pattern.compile("ondragover", Pattern.CASE_INSENSITIVE),
            Pattern.compile("ondragstart", Pattern.CASE_INSENSITIVE),
            Pattern.compile("ondrop", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onEnd", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onerror", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onerrorupdate", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onfilterchange", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onfinish", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onfocus", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onfocusin", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onfocusout", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onHashChange", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onhelp", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onInput", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onkeydown", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onkeypress", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onkeyup", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onlayoutcomplete", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onload", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onlosecapture", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onMediaComplete", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onMediaError", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onMessage", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onmousedown", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onmouseenter", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onmouseleave", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onmousemove", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onmouseout", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onmouseover", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onmouseup", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onmousewheel", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onmove", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onmoveend", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onmovestart", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onOffline", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onOnline", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onOutOfSync", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onpaste", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onPause", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onPopState", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onProgress", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onpropertychange", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onreadystatechange", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onRedo", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onRepeat", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onreset", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onresize", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onresizeend", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onresizestart", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onResume", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onReverse", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onRowDelete", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onrowenter", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onrowexit", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onRowInserted", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onrowsdelete", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onRowsEnter", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onrowsinserted", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onscroll", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onSeek", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onselect", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onselectionchange", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onselectstart", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onstart", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onsto", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onStorage", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onsubmit", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onSyncRestored", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onTimeError", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onTrackChange", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onUndo", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onunload", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onURLFlip", Pattern.CASE_INSENSITIVE),
            Pattern.compile("refresh", Pattern.CASE_INSENSITIVE),
            Pattern.compile("res://", Pattern.CASE_INSENSITIVE),
            Pattern.compile("script", Pattern.CASE_INSENSITIVE),
            Pattern.compile("ScRiPt%20%0a%0d", Pattern.CASE_INSENSITIVE),
            Pattern.compile("seekSegmentTime", Pattern.CASE_INSENSITIVE),
            Pattern.compile("string", Pattern.CASE_INSENSITIVE),
            Pattern.compile("title", Pattern.CASE_INSENSITIVE),
            Pattern.compile("vbscript", Pattern.CASE_INSENSITIVE),
            Pattern.compile("void", Pattern.CASE_INSENSITIVE),
            Pattern.compile("wvs-xss", Pattern.CASE_INSENSITIVE),
            Pattern.compile("xml", Pattern.CASE_INSENSITIVE),
            Pattern.compile("xmlns:html", Pattern.CASE_INSENSITIVE),
            Pattern.compile("xmlns:html=", Pattern.CASE_INSENSITIVE),
            Pattern.compile("x-scriptlet", Pattern.CASE_INSENSITIVE)
    };

    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }

        return encodedValues;
    }

    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }

        return cleanXSS(value);
    }

    public CustomXSSRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null)
            return null;

        return cleanXSS(value);
    }

    private String cleanXSS(String value) {
        if (value != null) {
            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
            // avoid encoded attacks.
            // value = ESAPI.encoder().canonicalize(value);

            // Avoid null characters
            value = value.replaceAll("", "");

            // Remove all sections that match a pattern
            for (Pattern scriptPattern : patterns) {
                value = scriptPattern.matcher(value).replaceAll("_REPLACED_");
            }
            value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
            value = value.replaceAll("#", "&#35;").replaceAll("\0", " ");
        }

        return value;
    }
}
