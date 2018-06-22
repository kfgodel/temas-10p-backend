## Temas-10p-backend
Configuracion 
####Para levantar la DB local 
    docker run --rm  -p 5433:5432 -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -e POSTGRES_DB=votacion  postgres
####Deploy en heroku (despues de instalar toolbelt)
     heroku create temas-roots-10p
     git push heroku master
     heroku ps:scale web=1
     heroku open
     heroku logs --tail

####Para deployar el frontend estoy haciendo
    ember build --environment production
    mvn deploy

 despues por ahi modifico este archivo para que heroku deploye nuevamente
    4

    git push heroku master