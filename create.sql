CREATE TABLE professor (
  id varchar(3),
  name varchar(10),
  department varchar(20),
  PRIMARY KEY (id),
  INDEX idx_professor_name (name),
);


CREATE TABLE course (
  department varchar(20),
  prof_id varchar(3),
  PRIMARY KEY (id),
  FOREIGN KEY (prof_id) REFERENCES professor(id),
  INDEX idx_course_title (title),
);

CREATE TABLE student (
  id varchar(7),
  name varchar(10),
  department varchar(20),
  PRIMARY KEY (id),
  INDEX idx_student_name (name),
);

CREATE TABLE rating (
  PRIMARY KEY (rating_id),
  FOREIGN KEY (course_id) REFERENCES course(id),
  FOREIGN KEY (prof_id) REFERENCES professor(id),
  FOREIGN KEY (student_id) REFERENCES student(id),
  INDEX idx_rating_course (point)
);