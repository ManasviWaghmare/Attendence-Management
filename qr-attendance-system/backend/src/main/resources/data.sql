-- Seed Data for H2
-- Insert Teachers
INSERT INTO teachers (id, employee_id, name, email, password) 
VALUES (1, 'T101', 'Dr. Alan Turing', 'alan.turing@example.edu', 'password123');
INSERT INTO teachers (id, employee_id, name, email, password) 
VALUES (2, 'T102', 'Grace Hopper', 'grace.hopper@example.edu', 'password123');

-- Insert Students
INSERT INTO students (id, roll_number, name, default_email, device_id)
VALUES (1, 'S001', 'John Doe', 'john.doe@example.edu', 'DEVICE_A101');
INSERT INTO students (id, roll_number, name, default_email, device_id)
VALUES (2, 'S002', 'Jane Smith', 'jane.smith@example.edu', 'DEVICE_B202');
INSERT INTO students (id, roll_number, name, default_email, device_id)
VALUES (3, 'S003', 'Bob Johnson', 'bob.johnson@example.edu', 'DEVICE_C303');
INSERT INTO students (id, roll_number, name, default_email, device_id)
VALUES (4, 'S004', 'Alice Williams', 'alice.williams@example.edu', 'DEVICE_D404');

-- Insert Courses
INSERT INTO courses (id, course_code, course_name, teacher_id)
VALUES (1, 'CS101', 'Intro to Algorithms', 1);
INSERT INTO courses (id, course_code, course_name, teacher_id)
VALUES (2, 'CS202', 'Advanced Java', 1);
INSERT INTO courses (id, course_code, course_name, teacher_id)
VALUES (3, 'CS303', 'Database Systems', 2);

-- Link Students to Courses
-- CS101: John, Jane, Bob
INSERT INTO course_students (course_id, student_id) VALUES (1, 1);
INSERT INTO course_students (course_id, student_id) VALUES (1, 2);
INSERT INTO course_students (course_id, student_id) VALUES (1, 3);

-- CS202: Jane, Alice
INSERT INTO course_students (course_id, student_id) VALUES (2, 2);
INSERT INTO course_students (course_id, student_id) VALUES (2, 4);

-- CS303: John, Bob, Alice
INSERT INTO course_students (course_id, student_id) VALUES (3, 1);
INSERT INTO course_students (course_id, student_id) VALUES (3, 3);
INSERT INTO course_students (course_id, student_id) VALUES (3, 4);
