insert into users(email, password, role) values
                                             ('admin.com','123','ADMIN'),
                                             ('client.com','123','CLIENT'),
                                             ('clientNotTask.com','123','CLIENT');
insert into task(
    title, details, status, priority, author_id, executor_id
) values ('title','details','PENDING','LOW',1,2);
insert into comment(details,task_id,author_id) values ('details in title 1',1,1)

