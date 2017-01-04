# CS496Assignment2

Project2<깨깨오톡> 이준수, 신윤열<br>
Tab A: <연락처 통합><br>
폰의 연락처 정보와 Facebook 연락처를 통합해 서버(Node.js) DB(Mongodb)에 기록해 listview로 표현했습니다.<br>가져오도록 한 정보는 이름, 핸드폰 번호(존재하면)입니다.<br>
앱을 켜자마자 폰의 연락처 정보를 불러올 수 있도록 했습니다.<br><br>
Tab B: <갤러리><br>
폰 갤러리 사진이나 사진을 찍어서, 이 사진들을 선택해 사진 서버 DB에 업로드한 후에, 이 사진들을 다시 다운로드해 어플에 띄울 수 있도록 했습니다.<br><br>

Tab C: <깨깨오톡><br>
탭 C에 들어가면 Dialog를 통해 Username을 묻고 (모두의) 채팅방에 들어가게 됩니다. 다른 사람과 즐겁게 채팅을 할 수 있습니다.<br>
내장된 기본 이모티콘까지 보낼 수 있습니다.

서버: NodeJS<br>
DB: MongoDB

내부 구현 방식

A탭 내부 구현 방식: 연락처를 가져올 때 Graph API의 URL을 요청함 - 수정 요망<br>
C탭 내부 구현 방식: Google Firebase를 사용함
