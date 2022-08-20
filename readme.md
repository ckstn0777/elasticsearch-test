### 개요
- ElasticSearch Getting Started
- ElasticSearch를 간단히 실습해보고 알아보기 위한 목적
- 추가) 엘라스틱 서치 검색 엔진 만들어보기 할 예정임

<br>

### 실습 환경 구성
1. 일단 로컬이든 어디든 엘라스틱 서치 구성을 해야겠지? -> 도커 컴포즈 사용
2. 그리고 나서 데이터를 수집해서 넣어야겠지?
    - 데이터 수집 : spring-batch, mariadb 사용
    - 엘라스틱 bulk 
3. 마지막으로 검색 엔진을 만들어보는 테스트를 진행하면 됨...

참고로 mac 기준으로 실습 환경 구성 진행했다. 현재는 엘라스틱 bulk까지 완료한 상태.

<br>

### 개발 과정 정리
- 노션 : https://ckstn0777.notion.site/spring-batch-2022-2f5fd448c4404dfd8e7781e250ca1bc1

<br>

### 참고 사이트
- 공식 사이트 : [https://www.elastic.co/kr/elasticsearch/](https://www.elastic.co/kr/elasticsearch/)
- 공식 문서 가이드 : [https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html](https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html)
- Elastic 가이드북(한국어) : [https://esbook.kimjmin.net/01-overview/1.1-elastic-stack/1.1.1-elasticsearch](https://esbook.kimjmin.net/01-overview/1.1-elastic-stack/1.1.1-elasticsearch)
- Install Elasticsearch with Docker : [https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html#\_pulling_the_image](https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html#_pulling_the_image)
- [https://velog.io/@jeff0720/Elasticsearch-이해와-로그-서버-구축-실습으로-핵심-개념-익히기](https://velog.io/@jeff0720/Elasticsearch-%EC%9D%B4%ED%95%B4%EC%99%80-%EB%A1%9C%EA%B7%B8-%EC%84%9C%EB%B2%84-%EA%B5%AC%EC%B6%95-%EC%8B%A4%EC%8A%B5%EC%9C%BC%EB%A1%9C-%ED%95%B5%EC%8B%AC-%EA%B0%9C%EB%85%90-%EC%9D%B5%ED%9E%88%EA%B8%B0)
