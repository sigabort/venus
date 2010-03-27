use `venustest`;
insert into users set username='venus', password='venus', status=0, instituteid=1, created=now(), lastmodified=now();
insert into user_roles set user=(select ID from users where username='venus'), role=0, status=0, department=NULL, created=now(), lastmodified=now();
