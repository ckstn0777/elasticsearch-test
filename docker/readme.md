### 도커 컴포즈 명세
- version : 도커 컴포즈 파일 버전
- image : 가장 중요한 도커 이미지 지정
- ports : 포트포워딩 지정 옵션 <호스트포트>:<컨테이너 포트>로 작성
- volumes : 바인드 마운트, 볼륨을 지정 <호스트경로>:<컨테이너경로>
- environment : 컨테이너에서 사용할 환경변수 지정
- depends_on : 의존성 명시 (여기서는 엘라스틱서치 이후 키바나가 동작되도록 설정)


### 볼륨설정 - volumes
- driver: local 이란 의미는 `docker volumne create`를 사용해서 볼륨을 생성한다는 뜻
- `docker volume ls`를 통해 볼륨 리스트 확인
- `docker volume inspect 볼륨명`을 통해 상세 정보 확인 가능
- 여기서 Mountpoint가 있는데 호스트 볼륨 경로이지만 호스트 OS는 액세스 할 수 없다고 한다. 실제로 해당 경로를 찾아봤지만 찾을 수 없었다... (이러면 좀 무쓸모아닌가)
- 해당 이유 참고 : https://forums.docker.com/t/var-lib-docker-does-not-exist-on-host/18314


### 네트워크설정 - networks
- `docker network ls`를 통해 네트워크 리스트 확인
- 따로 추가한게 없으면 bridge, host, noen은 기본 네트워크
- elastic: driver: bridge를 통해 기본 bridge가 아닌 새 bridge를 생성하므로 docker_elastic이란 네트워크가 새로 생성되었다.
- `docker network inspect network명`을 통해 상세 정보 확인가능 (여기서 Containers 확인하면 컨테이너 별로 할당된 ip를 확인할 수 있다. 기본 172.20.0.2 순서로 할당됨)
- host와 container간 통신이 이뤄지려면 중간자 역할(포트포워딩?)이 필요한데 그게 docker0 인듯. (mac에선 안보이지만...)


### 도커 컴포즈 명령어

```
백그라운드에서 실행 : docker-compose up -d
서비스 중지 : docker-compose stop
서비스 다운 : docker-compose down
```


### 도커 명령어
```
도커 컨테이너 리스트 : docker ps
```