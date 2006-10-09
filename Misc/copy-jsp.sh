#!/bin/sh

scp -ri ~/rsa_id ~/ohtu/kohahdus/EAssari/WEB-INF/classes/* tkt_koha@db:/home/tkt_koha/tomcat/webapps/EAssari/WEB-INF/classes/
scp -ri ~/rsa_id ~/ohtu/kohahdus/EAssari/www/* tkt_koha@db:/home/tkt_koha/tomcat/webapps/EAssari/www/
