#!/bin/sh

scp -ri ~/rsa_id ~/workspace/kohahdus/EAssari/WEB-INF/classes/* tkt_koha@db:/home/tkt_koha/tomcat/webapps/EAssari/WEB-INF/classes/
scp -ri ~/rsa_id ~/workspace/kohahdus/EAssari/www/* tkt_koha@db:/home/tkt_koha/tomcat/webapps/KONTEKSTI/www/
scp -ri ~/rsa_id ~/workspace/kohahdus/EAssari/WEB-INF/xml/* tkt_koha@db:/home/tkt_koha/tomcat/webapps/KONTEKSTI/WEB-INF/xml/
scp -ri ~/rsa_id ~/workspace/kohahdus/EAssari/styles/* tkt_koha@db:/home/tkt_koha/tomcat/webapps/KONTEKSTI/styles/