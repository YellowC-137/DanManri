# CAP

단국대 분실물 어플

10/7 일전까지
-Firebase 연동 O
-Google Map API 연동
-게시판 + 전체 UI 만들기 O
-분실과 습득 게시판 매칭 기능 구현과정 생각

유튜브 안드로이드 스튜디오
https://www.youtube.com/playlist?list=PLG7te9eYUi7sjJzJR2i5m6wv-X_7K2pVE : 기초강의
https://www.youtube.com/playlist?list=PLva6rQOdsvQXdSBN1r2mEt_tqES6NjKKj : 개별강의

FireBase 문서
https://firebase.google.com/docs/android/setup

구글맵 문서
https://developers.google.com/maps/documentation/android-sdk/overview?hl=ko

안드로이드 문서
https://developer.android.com/docs?hl=ko

임시로 FifeBase console 연결한것
https://console.firebase.google.com/project/lostnfound-3024f/overview

구글맵 API 연결, 지도에 마커 표시
https://mailmail.tistory.com/category/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C/9.%20%EA%B5%AC%EA%B8%80%EB%A7%B5%20API

firebase 기반 채팅
https://medium.com/@nanyoung18/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%8A%A4%ED%8A%9C%EB%94%94%EC%98%A4-%EA%B8%B0%EC%B4%88-6-%EC%B1%84%ED%8C%85-%EC%95%B1-%EB%A7%8C%EB%93%A4%EA%B8%B0-97c8f83e5bdf

구글맵은 일단 내 계정으로 만들기는 했으나 (체험판)
문서를 좀더 찾아보고 제대로 연결해야 할듯

로그인 인증 - 마이크로소프트 학교계정 , 전화인증(?), 학생증 

비밀번호/ 찾기?
ID는 학번@dankook.ac.kr 고정?

Fragment가 아닌 그냥 Activity로 구현하기?


10.05
로그인시 id,pw 경우 설정 해야함
슬라이드뷰가 아닌 버튼으로 lost,found 설정?
progressbar 설정 - 전역

Warning:(23, 9) Hardcoded string "Notify page", should use `@string` resource
Warning:(31, 18) Casting 'findViewById(...)' to 'EditText' is redundant
Warning:(24, 17) 'androidx.lifecycle.ViewModelProviders' is deprecated
몇몇 warning들 확인

10/06
Fragment - Home(게시판),notify(알림?-추후 수정예정),map,setting fragment
스크롤기능 X : 없어도 되는지 모르겠음, 추후에 추가
HomeActivity crash
게시판 글쓰기 까지 구현, 게시판 글모음 보기 X

11.03
설정 - 로그아웃, 회원탈퇴 추가

11.11
알림 - 키워드 추가 액티비티
KeywordBack.java
-> 시작할 때 키워드를 입력받고 새 포스트의 제목 혹은 내용이 키워드와 일치할 시 알림 발생
-> 포스트가 자신이 입력한 것일 경우 종료 후 다시 시작
-> 키워드가 중간에 변경될 시 다시 시작되게 변경해야 될 듯

HomeActivity에서 서비스 (KeywordBack.java) 실행
-> 나중에 SharedPreferences 이용해서 On / Off 가능하게 바꿈


===11.12_현규===
1. Found와 Lost 두개 따로 게시판을 만들었음.
-> FoundFragment와 LostFragment에서 생성한 글은 따로 보인다.
-> 이때 전용 Writing Activity를 각각 두어 FoundWritingActivity와 LostWritingActivity가 존재한다.

2. Writing 레이아웃을 수정하였음.
-> 관계형 레이아웃에서 Relatinvelayout + Linearlayout의 레이아웃으로 변경함
-> 추가로 수정해도 됨. 가장 깔끔한 UI는 작은 이미지로 카메라, 맵을 넣어서 버튼을 작게만드는게 좋아보임.

3. 게시글 클릭시 읽을 수 있음
-> 기존 올라온 글들은 코딩에 오류가 있었음. documentid가 userid로 되어있어서 볼 수 없음
-> 게시글을 보는 레이아웃을 추가로 수정해야함

4. 게시글 꾸욱 누르면 삭제 가능함.
-> 삭제 권한에 대한 점은 추가해야할 듯. 다른 사람이 쓴 글도 아마 삭제될 것임
-> 이에대한 UI도 기본적인 것을 사용했음. 필요하다면 수정

5. 회원가입시에 유저에 관한 정보들도 같이 들어감.
-> 일단 간단한 정보들을 들어가도록 해놓음. 닉네임이나 새로운 정보들을 추가해도 될 듯

6. 변수명 통일과 자바파일 패키지로 정리
-> 변수명 FㅐundActivity와 LostActivity의 변수명(fStore나, lStroe)를 통일함
-> ui > home > 에 found와 lost 패키지를 만들고 Writing과 post Activity를 넣어놨음 

11/13
준성
채팅기능,아이콘 추가
일단 지금은 설정부분에 쪽지함으로 가게 해둠 (나중에 게시글처럼 해두면 될듯? 시간순으로)
ID가져오는데 null오류가 날거임, 이부분은 게시글에 쪽지보내기 버튼 추가하면서 고침
Intent 제대로 연결하면 정상적으로 작동함.

===11/13_현규===
1. 게시글에 올라오는 item_post_layout을 살짝 수정함
-> Realative + Linear로 3줄짜리로 만듦
-> 추가 할 내용은 Time, name, 댓글과 추천수

2. 제목과 내용을 표기할 때 한줄을 max로 하고 넘칠경우 뒷부분을 ... 로 표현함
-> 글쓰기 할 때도 제목은 1줄을 max로 잡고 최대 34글자 까지 입력 가능함.
-> 글 클릭했을 때 Title 글씨가 살짝 보이는거 수정함. 초기에 입력된 부분을 삭제한거라 레이아웃에선 투명하게 보임


11.15 주현
회원가입 시 학번, 이름 받음 (Firestore 참조)
및 회원가입 액티비티 


===11/23_현규===
1. 게시글 등록시에 앨범에서 사진을 업로드 할 수 있음(카메라도 선택할 수 있도록 구현해보려다가 실패함)
2. item_post 레이아웃 수정. 
3. 글쓰기 제목을 singleLine으로 했던게 사라져서 다시 만듦
4. 글쓰기 레이아웃 아랫부분에 이미지 넣는 공간을 만들어놓음
