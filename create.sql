CREATE TABLE professor (
  id varchar(3),
  name varchar(10),
  department varchar(20),
  PRIMARY KEY (id)
);


CREATE TABLE course (
  id varchar(6),
  title varchar(20),
  department varchar(20),
  PRIMARY KEY (id)
);


CREATE TABLE student (
  id varchar(7),
  name varchar(10),
  department varchar(20),
  PRIMARY KEY (id)
);

CREATE TABLE rating (
  rating_id int,
  course_id varchar(6),
  prof_id varchar(3),
  student_id varchar(7),
  point int,
  contents varchar(100),
  PRIMARY KEY (rating_id),
  FOREIGN KEY (course_id) REFERENCES course (id),
  FOREIGN KEY (prof_id) REFERENCES professor (id),
  FOREIGN KEY (student_id) REFERENCES student (id)
);
