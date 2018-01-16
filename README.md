# XSS Filter memory performance test
Servlet에 XSS Filter 적용시의 memory 영향도를 간단하게 테스트 하기 위한 Spring Boot 프로젝트입니다

## lucy-xss-servlet-filter
naver에서 개발한 lucy-xss를 serlvet-filter로 적용한 OSS

## Custom XSS Filter
XSS를 위한 가장 기본적인 filter 구현체 예제

## 테스트
1. -Xms512m -Xmx512m 옵션을 주고 프로젝트 실행
2. jstat을 이용 하여 GC 횟수 및 소요 시간을 수치로 확인
3. VirtualVM을 실행 하여 해당 프로세스의 Virtual GC 모니터링 (옵션)
4. jMeter로 간단한 부하 실행

테스트 정보:
 - 로드 시간: 300초
 - POST body
 ```test=<script>alert(1);<script> ```

## 결과
jstat 으로 GC 수치를 확인
```
jstat -gcutil -h10 <pid> 1000
```
#### Custom XSS Filter
Custom XSS Filter와 같이 기본 구현체를 이용하여 적용(패턴: 200여 개) 할 경우 memory 측면에서 과도한 minor GC를 발생 시킴

시작:\
  S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT \
  0.00  40.20  13.39   3.67  98.31  94.60      3    0.034     1    0.033    0.067

종료:\
  S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT \
  0.00   7.03  10.00   5.97  97.48  94.68     47    0.171     1    0.033    0.204
- minor GC 평균 소요 시간: 4ms
- minor GC 횟수: 44
- minor GC 횟수(10초당): 1.4/10s
- major GC 평균 소요 시간: 0
- major GC 횟수: 0
- 총 GC 평균 소요 시간: 4ms

#### lucy-xss-servlet-filter
lucy-xss-servlet-filter의 경우 Custom XSS Filter 보다 memory 측면에서 성능이 좋음

시작:\
  S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT \
  0.00  39.37  10.65   3.58  97.57  94.76      3    0.029     1    0.032    0.061

종료:\
  S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT \
  5.95   0.00  73.16   5.59  97.33  95.24     30    0.142     1    0.032    0.174

- minor GC 평균 소요 시간: 4ms
- minor GC 횟수: 27
- minor GC 횟수(10초당): 0.9/10s
- major GC 평균 소요 시간: 0
- major GC 횟수: 0
- 총 GC 평균 소요 시간: 4ms

#### Custom XSS vs lucy
짧은 시간의 로드와 단순한 파라메터로 테스트 하였지만 Custom XSS의 minor GC 횟수가 lucy 대비 높은 것을 알 수 있다.
lucy는 Custom XSS 대비 minor GC 횟수가 0.5/10s 정도 좋은 효율을 보이고 있다.

Custom XSS의 경우에는 200여개의 pattern이 등록 되어있어 이로 인한 메모리 사용량이 크다.

두 테스트 방식 간에 pattern의 범위를 맞추고 테스트를 해볼 필요가 있으나 수치의 폭이 줄어 들뿐
Custom XSS(pattern 20여개 )가 lucy 대비 메모리 사용량이 줄어들진 않는다.

기능의 범위, pattern 적용 방식, 안정성, 성능 등을 고려 하였을 때 lucy를 사용 하는 것을 추천.

## Next
ESAPI 적용 및 비교
pattern 개수, parameter의 개수 등을 증가 하여 세밀화된 테스트가 필요 할 수
