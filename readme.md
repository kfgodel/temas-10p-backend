## Temas-10p-backend

Deploy en heroku (despues de instalar toolbelt)
- heroku create temas-roots-10p
- git push heroku master
- heroku ps:scale web=1
- heroku open
- heroku logs --tail

Para deployar el frontend estoy haciendo
ember build --production..... algo asi
mvn deploy

Y despues por ahi modifico este archivo para que heroku deploye nuevamente