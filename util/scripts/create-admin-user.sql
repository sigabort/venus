use `venustest`;
insert into institutes set name='venus', code='venus', status=0, created=now(), lastmodified=now();
insert into users set username='venus', password='venus', status=0, institute=1, created=now(), lastmodified=now();
insert into user_roles set user=(select ID from users where username='venus'), role=0, status=0, department=NULL, created=now(), lastmodified=now();
