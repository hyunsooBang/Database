CREATE TABLE professor (
  id varchar(3),
  name varchar(100),
  department varchar(200),
  PRIMARY KEY (id)
);

CREATE TABLE course (
  id varchar(6),
  title varchar(200),
  department varchar(200),
  prof_id varchar(3),
  PRIMARY KEY (id),
  FOREIGN KEY (prof_id) REFERENCES professor(id)
);

CREATE TABLE student (
  id varchar(7),
  name varchar(200),
  department varchar(200),
  PRIMARY KEY (id)
);

CREATE TABLE rating (
  rating_id int AUTO_INCREMENT,
  course_id varchar(6),
  prof_id varchar(3),
  student_id varchar(7),
  point numeric(3,2),
  contents varchar(1000),
  PRIMARY KEY (rating_id),
  FOREIGN KEY (course_id) REFERENCES course(id),
  FOREIGN KEY (prof_id) REFERENCES professor(id),
  FOREIGN KEY (student_id) REFERENCES student(id)
);

CREATE INDEX course_id_index ON course (id);
CREATE INDEX professor_id_index ON professor (id);
CREATE INDEX student_id_index ON student (id);
