-- ============================================================
-- JOB PORTAL DATABASE SCHEMA
-- ============================================================

-- 1. USERS_TYPE Table
CREATE TABLE users_type (
    userTypeId INT PRIMARY KEY AUTO_INCREMENT,
    userTypeName VARCHAR(255) NOT NULL
);

-- 2. USERS Table
CREATE TABLE users (
    userId INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    isActive BOOLEAN DEFAULT true,
    registrationDate DATE,
    usersTypeId INT NOT NULL,
    FOREIGN KEY (usersTypeId) REFERENCES users_type(userTypeId) ON CASCADE DELETE
);

-- 3. JOB_SEEKER_PROFILE Table
CREATE TABLE job_seeker_profile (
    userAccountId INT PRIMARY KEY,
    firstName VARCHAR(255),
    lastName VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    country VARCHAR(255),
    workAuthorization VARCHAR(255),
    employmentType VARCHAR(255),
    resume LONGTEXT,
    profilePhoto VARCHAR(64),
    FOREIGN KEY (userAccountId) REFERENCES users(userId) ON DELETE CASCADE
);

-- 4. RECRUITER_PROFILE Table
CREATE TABLE recruiter_profile (
    userAccountId INT PRIMARY KEY,
    firstName VARCHAR(255),
    lastName VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    country VARCHAR(255),
    company VARCHAR(255),
    profilePhoto VARCHAR(64),
    FOREIGN KEY (userAccountId) REFERENCES users(userId) ON DELETE CASCADE
);

-- 5. LOCATION Table
CREATE TABLE location (
    id INT PRIMARY KEY AUTO_INCREMENT,
    city VARCHAR(255),
    state VARCHAR(255),
    country VARCHAR(255)
);

-- 6. COMPANY Table
CREATE TABLE company (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    logo VARCHAR(255)
);

-- 7. POST Table (Job Postings)
CREATE TABLE post (
    postId INT PRIMARY KEY AUTO_INCREMENT,
    jobTitle VARCHAR(255),
    description LONGTEXT,
    jobType VARCHAR(255),
    salary VARCHAR(255),
    remote VARCHAR(255),
    postedDate DATE,
    recuriterId INT NOT NULL,
    jobLocationId INT NOT NULL,
    jobCompanyId INT NOT NULL,
    FOREIGN KEY (recuriterId) REFERENCES users(userId) ON DELETE CASCADE,
    FOREIGN KEY (jobLocationId) REFERENCES location(id) ON DELETE CASCADE,
    FOREIGN KEY (jobCompanyId) REFERENCES company(id) ON DELETE CASCADE
);

-- 8. SKILLS Table
CREATE TABLE skills (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    experience VARCHAR(255),
    yearsOfExperience VARCHAR(255),
    candidate_profile INT NOT NULL,
    FOREIGN KEY (candidate_profile) REFERENCES job_seeker_profile(userAccountId) ON DELETE CASCADE
);

-- 9. SAVED_JOBS Table (Bookmark/Favorite Jobs)
CREATE TABLE saved_jobs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    userId INT NOT NULL,
    job INT NOT NULL,
    FOREIGN KEY (userId) REFERENCES job_seeker_profile(userAccountId) ON DELETE CASCADE,
    FOREIGN KEY (job) REFERENCES post(postId) ON DELETE CASCADE,
    UNIQUE KEY unique_saved_job (userId, job)
);

-- 10. APPLICATION Table (Job Applications)
CREATE TABLE application (
    id INT PRIMARY KEY AUTO_INCREMENT,
    candidate INT NOT NULL,
    post INT NOT NULL,
    applyDate DATE,
    coverLetter LONGTEXT,
    FOREIGN KEY (candidate) REFERENCES job_seeker_profile(userAccountId) ON DELETE CASCADE,
    FOREIGN KEY (post) REFERENCES post(jobPostId) ON DELETE CASCADE,
    UNIQUE KEY unique_application (candidate, post)
);

-- ============================================================
-- KEY RELATIONSHIPS
-- ============================================================
/*
1. USERS_TYPE (1) ──────── (N) USERS
   - One user type can have many users

2. USERS (1) ──────---- (1) JOB_SEEKER_PROFILE
   - One user can have one job seeker profile

3. USERS (1) ──────---- (1) RECRUITER_PROFILE
   - One user can have one recruiter profile

4. JOB_SEEKER_PROFILE (1) ──── (N) SKILLS
   - One candidate can have many skills

5. JOB_SEEKER_PROFILE (1) ──── (N) SAVED_JOBS
   - One candidate can save many jobs

6. JOB_SEEKER_PROFILE (1) ──── (N) APPLICATION
   - One candidate can apply to many jobs

7. USERS (1) ──── (N) POST
   - One recruiter can post many jobs

8. POST (1) ──── (N) SAVED_JOBS
   - One job can be saved by many candidates

9. POST (1) ──── (N) APPLICATION
   - One job can receive many applications

10. LOCATION (1) ──── (N) POST
    - One location can have many job postings

11. COMPANY (1) ──── (N) POST
    - One company can have many job postings
*/

-- ============================================================
-- SAMPLE DATA FOR TESTING
-- ============================================================

INSERT INTO users_type (userTypeName) VALUES ('Recruiter');
INSERT INTO users_type (userTypeName) VALUES ('Job Seeker');

-- Recruiter user
INSERT INTO users (email, password, isActive, registrationDate, usersTypeId)
VALUES ('recruiter@jobportal.com', 'hashed_password', true, NOW(), 1);

-- Job Seeker user
INSERT INTO users (email, password, isActive, registrationDate, usersTypeId)
VALUES ('jobseeker@jobportal.com', 'hashed_password', true, NOW(), 2);

-- Recruiter profile
INSERT INTO recruiter_profile (userAccountId, firstName, lastName, city, state, country, company)
VALUES (1, 'John', 'Recruiter', 'New York', 'NY', 'USA', 'Tech Corp');

-- Job Seeker profile
INSERT INTO job_seeker_profile (userAccountId, firstName, lastName, city, state, country, workAuthorization, employmentType)
VALUES (2, 'Jane', 'Seeker', 'San Francisco', 'CA', 'USA', 'H1B', 'Full-Time');

-- Location
INSERT INTO location (city, state, country) VALUES ('New York', 'NY', 'USA');

-- Company
INSERT INTO company (name, logo) VALUES ('Tech Corp', 'logo.png');

-- Job Posting
INSERT INTO post (jobTitle, description, jobType, salary, remote, postedDate, recuriterId, jobLocationId, jobCompanyId)
VALUES ('Senior Java Developer', 'We are looking for...', 'Full-Time', '$100,000-$150,000', 'Yes', NOW(), 1, 1, 1);

-- Skills
INSERT INTO skills (name, experience, yearsOfExperience, candidate_profile)
VALUES ('Java', 'Advanced', '5', 2);

-- ============================================================
-- USEFUL QUERIES
-- ============================================================

-- Get all jobs posted by a recruiter
-- SELECT p.* FROM post p WHERE p.recuriterId = ?;

-- Get all applications for a specific job
-- SELECT a.*, js.firstName, js.lastName FROM application a
-- JOIN job_seeker_profile js ON a.candidate = js.userAccountId
-- WHERE a.post = ?;

-- Get saved jobs by a candidate
-- SELECT p.*, c.firstName FROM saved_jobs sj
-- JOIN post p ON sj.job = p.postId
-- JOIN job_seeker_profile c ON sj.userId = c.userAccountId
-- WHERE sj.userId = ?;

-- Get candidate profile with skills
-- SELECT jsp.*, s.name, s.yearsOfExperience FROM job_seeker_profile jsp
-- LEFT JOIN skills s ON jsp.userAccountId = s.candidate_profile
-- WHERE jsp.userAccountId = ?;



