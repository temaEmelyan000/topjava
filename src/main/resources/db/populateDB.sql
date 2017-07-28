DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password');

INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (date_time, calories, description, user_id) VALUES
  ('2017-07-28T22:44', 200, 'lol', 100000),
  ('2017-07-28T22:45', 200, 'lol', 100000),
  ('2017-07-28T22:46', 200, 'lol', 100000),
  ('2017-07-28T22:47', 200, 'lol', 100000),
  ('2017-07-28T22:48', 200, 'lol', 100000),
  ('2017-07-28T22:49', 200, 'lol', 100000),
  ('2017-07-28T22:50', 200, 'lol', 100000),
  ('2017-07-28T22:51', 200, 'lol', 100000),
  ('2017-07-28T22:52', 200, 'lol', 100000),
  ('2017-07-28T22:53', 200, 'lol', 100000),
  ('2017-07-28T22:54', 200, 'lol', 100000);
