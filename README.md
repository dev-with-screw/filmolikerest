# filmolikerest
Second project for demonstrating skills of programming

Инструкция пользователя

1) Зарегистрировать новую учетку:
   - POST на эндпоинт http://localhost:8087/register
   - передать json {"username":"<ТЕКСТ>", "password":"<ТЕКСТ>", "email":"<Какой-нибуль электронный адрес>", "firstname":"<ТЕКСТ>", "lastname":"<ТЕКСТ>"}
   
2) Получить токен:
   - POST на эндпоинт http://localhost:8087/login
   - передать json {"username":"<ТЕКСТ>", "password":"<ТЕКСТ>"}
   
3) Создать новую заметку:
   - POST на эндпоинт http://localhost:8087/note
   - передать json {"title":"<ТЕКСТ>", "watched":"true/false", "estimate":"NOT_ESTIMATE/POOR/AVERAGE/GOOD/EXCELENT"}
   
4) Просмотреть созданные заметки:
   - GET на эндпоинт http://localhost:8087/notes
   
5) Просмотреть одну заметку:
   - GET на эндпоинт http://localhost:8087/note/<номер>
   
6) Отредактировать заметку:
   - PUT на эндпоинт http://localhost:8087/note/<номер>
   - передать json {"title":"<ТЕКСТ>", "watched":"true/false", "estimate":"NOT_ESTIMATE/POOR/AVERAGE/GOOD/EXCELENT"}
   
7) Удалить заметку:
   - DELETE на эндпоинт http://localhost:8087/note/<номер>
