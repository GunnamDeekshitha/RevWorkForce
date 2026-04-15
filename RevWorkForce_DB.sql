-- Database creation
create database revworkforce;
use revworkforce;

-- Employee table creation
create table employee(
    employee_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    email VARCHAR(100),
    password VARCHAR(100),
    phone VARCHAR(15),
    address VARCHAR(255),
    role VARCHAR(20),
    manager_id INT,
    department VARCHAR(50),
    designation VARCHAR(50),
    salary DOUBLE,
    status VARCHAR(20)
);

-- Leave request table
create table leave_request(
    leave_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT,
    leave_type VARCHAR(20),
    start_date DATE,
    end_date DATE,
    reason VARCHAR(255),
    status VARCHAR(20),
    manager_comment VARCHAR(255),
    FOREIGN KEY(employee_id) REFERENCES employee(employee_id)
);

-- Leave balance table
create table leave_balance(
    employee_id INT PRIMARY KEY,
    casual_leave INT DEFAULT 10,
    sick_leave INT DEFAULT 10,
    paid_leave INT DEFAULT 15,
    FOREIGN KEY(employee_id) REFERENCES employee(employee_id)
);

-- Performance review table
create table performance_review(
    review_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT,
    year INT,
    self_assessment TEXT,
    accomplishments TEXT,
    improvements TEXT,
    rating INT,
    manager_feedback TEXT,
    FOREIGN KEY(employee_id) REFERENCES employee(employee_id)
);

-- Goals table
create table goals(
    goal_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT,
    description TEXT,
    deadline DATE,
    priority VARCHAR(10),
    progress INT,
    status VARCHAR(20),
    FOREIGN KEY(employee_id) REFERENCES employee(employee_id)
);

-- Notifications table
create table notifications(
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT,
    message TEXT,
    status VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(employee_id) REFERENCES employee(employee_id)
);


CREATE TABLE holiday (
    holiday_id INT AUTO_INCREMENT PRIMARY KEY,
    holiday_date DATE,
    holiday_name VARCHAR(100)
);

CREATE TABLE goals (
    goal_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    description TEXT,
    deadline DATE,
    priority VARCHAR(20),
    status VARCHAR(20),
    FOREIGN KEY (employee_id) REFERENCES employee(employee_id)
);

CREATE TABLE announcements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE employee
ADD emergency_contact_name VARCHAR(100),
ADD emergency_contact_phone VARCHAR(15);

ALTER TABLE employee
ADD date_of_birth DATE,
ADD joining_date DATE;  

CREATE TABLE department (
    dept_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_name VARCHAR(100)
);
CREATE TABLE designation (
    desig_id INT AUTO_INCREMENT PRIMARY KEY,
    desig_name VARCHAR(100),
    dept_id INT,
    FOREIGN KEY (dept_id) REFERENCES department(dept_id)
);
ALTER TABLE employee
ADD dept_id INT,
ADD desig_id INT,
ADD FOREIGN KEY (dept_id) REFERENCES department(dept_id),
ADD FOREIGN KEY (desig_id) REFERENCES designation(desig_id);

CREATE TABLE announcements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
); 

ALTER TABLE designation
ADD CONSTRAINT fk_dept
FOREIGN KEY (dept_id)
REFERENCES department(dept_id);

ALTER TABLE designation
ADD CONSTRAINT unique_designation_dept UNIQUE (desig_name, dept_id);

ALTER TABLE department
ADD CONSTRAINT unique_dept_name UNIQUE (dept_name);

