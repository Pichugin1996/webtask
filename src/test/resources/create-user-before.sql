delete from wt_users;

INSERT INTO wt_users(id, password, role, status, username)
VALUES(10, '$2a$10$II22Pr/zT9JQJ8CjutKDhOt8wqOawlL4iRE3cRTqPz5my4f5hYf6O', 'ADMIN', 'ACTIVE', 'Admin');

INSERT INTO wt_users(id, password, role, status, username)
VALUES(11, '$2a$10$4tmtyYIn3vFE9UYIMBIqCeR5FUG7oVvNc1NjHD/DEi2nVL4rhugea', 'USER', 'ACTIVE', 'User');

