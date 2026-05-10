# Job Portal Database Schema

## Entity Overview

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         JOB PORTAL DATABASE SCHEMA                          │
└─────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────┐
│      USERS_TYPE          │
├──────────────────────────┤
│ userTypeId (PK)          │
│ userTypeName             │
└──────────────────────────┘
         ▲ 1
         │
         │ N
         │
┌──────────────────────────────────┐
│           USERS                  │ (users)
├──────────────────────────────────┤
│ userId (PK)                      │
│ email (UNIQUE)                   │
│ password                         │
│ isActive                         │
│ registrationDate                 │
│ usersTypeId (FK) ──────────────┐│
└──────────────────────────────────┘
         │
    ┌────┴──────────────────────────────────┐
    │                                       │
    │ 1:1                          1:N       │
    ▼                              ▼        ▼
┌──────────────────────────┐  ┌──────────────────────────┐
│   JOB_SEEKER_PROFILE     │  │  RECRUITER_PROFILE       │
│ (CandidateProfile)       │  │ (RecuiterProfile)        │
├──────────────────────────┤  ├──────────────────────────┤
│ userAccountId (PK/FK)    │  │ userAccountId (PK/FK)    │
│ firstName                │  │ firstName                │
│ lastName                 │  │ lastName                 │
│ city                     │  │ city                     │
│ state                    │  │ state                    │
│ country                  │  │ country                  │
│ workAuthorization        │  │ company                  │
│ employmentType           │  │ profilePhoto             │
│ resume                   │  └──────────────────────────┘
│ profilePhoto             │
└──────────────────────────┘
         │
         │ 1:N
         │
         ├─────────────────────────────────┐
         │                                 │
         ▼                                 ▼
┌──────────────────────────┐   ┌──────────────────────────┐
│       SKILLS             │   │    SAVED_JOBS            │
├──────────────────────────┤   ├──────────────────────────┤
│ id (PK)                  │   │ id (PK)                  │
│ name                     │   │ userId (FK)              │
│ experience               │   │ job (FK)                 │
│ yearsOfExperience        │   └──────────────────────────┘
│ candidate_profile (FK) ──┤           │
└──────────────────────────┘           │
                                       │ N:1
                                       │
                                       ▼
                            ┌──────────────────────────┐
                            │        POST              │
                            ├──────────────────────────┤
                            │ postId (PK)              │
                            │ jobTitle                 │
                            │ description              │
                            │ jobType                  │
                            │ salary                   │
                            │ remote                   │
                            │ postedDate               │
                            │ recuriterId (FK) ───────┐│
                            │ jobLocationId (FK) ─────┤│
                            │ jobCompanyId (FK) ──────┤│
                            │ isActive (Transient)     ││
                            │ isSaved (Transient)      ││
                            └──────────────────────────┘
                                   │
                            ┌──────┴──────────┐
                            │                 │
                            │ N:1             │ N:1
                            │                 │
                            ▼                 ▼
                    ┌──────────────────┐  ┌──────────────────┐
                    │   LOCATION       │  │    COMPANY       │
                    ├──────────────────┤  ├──────────────────┤
                    │ id (PK)          │  │ id (PK)          │
                    │ city             │  │ name             │
                    │ state            │  │ logo             │
                    │ country          │  └──────────────────┘
                    └──────────────────┘
                            ▲
                            │
                            │ N:1
                            │
                    ┌──────────────────────────┐
                    │    APPLICATION           │
                    ├──────────────────────────┤
                    │ id (PK)                  │
                    │ userId (FK) ──────────┐ │
                    │ job (FK) ─────────────┤ │
                    │ applyDate              │ │
                    │ coverLetter            │ │
                    └──────────────────────────┘
```

---

## Table Details

### 1. **USERS_TYPE** (users_type)
| Column | Type | Constraint |
|--------|------|-----------|
| userTypeId | INT | PK, AUTO_INCREMENT |
| userTypeName | VARCHAR | |

**Relationships:**
- 1:N with USERS

---

### 2. **USERS** (users)
| Column | Type | Constraint |
|--------|------|-----------|
| userId | INT | PK, AUTO_INCREMENT |
| email | VARCHAR | UNIQUE |
| password | VARCHAR | NOT NULL |
| isActive | BOOLEAN | |
| registrationDate | DATE | |
| usersTypeId | INT | FK → users_type.userTypeId |

**Relationships:**
- N:1 with USERS_TYPE
- 1:1 with JOB_SEEKER_PROFILE
- 1:N with RECRUITER_PROFILE
- 1:N with POST (recruiter)

---

### 3. **JOB_SEEKER_PROFILE** (job_seeker_profile)
| Column | Type | Constraint |
|--------|------|-----------|
| userAccountId | INT | PK, FK → users.userId |
| firstName | VARCHAR | |
| lastName | VARCHAR | |
| city | VARCHAR | |
| state | VARCHAR | |
| country | VARCHAR | |
| workAuthorization | VARCHAR | |
| employmentType | VARCHAR | |
| resume | VARCHAR | |
| profilePhoto | VARCHAR(64) | |

**Relationships:**
- 1:1 with USERS (@MapsId)
- 1:N with SKILLS
- 1:N with SAVED_JOBS
- 1:N with APPLICATION

---

### 4. **RECRUITER_PROFILE** (recruiter_profile)
| Column | Type | Constraint |
|--------|------|-----------|
| userAccountId | INT | PK, FK → users.userId |
| firstName | VARCHAR | |
| lastName | VARCHAR | |
| city | VARCHAR | |
| state | VARCHAR | |
| country | VARCHAR | |
| company | VARCHAR | |
| profilePhoto | VARCHAR(64) | |

**Relationships:**
- 1:1 with USERS (using @OneToMany with @MapsId - ⚠️ **ISSUE**)

---

### 5. **SKILLS** (skills)
| Column | Type | Constraint |
|--------|------|-----------|
| id | INT | PK, AUTO_INCREMENT |
| name | VARCHAR | |
| experience | VARCHAR | |
| yearsOfExperience | VARCHAR | |
| candidate_profile | INT | FK → job_seeker_profile.userAccountId |

**Relationships:**
- N:1 with JOB_SEEKER_PROFILE

---

### 6. **SAVED_JOBS** (saved_jobs)
| Column | Type | Constraint |
|--------|------|-----------|
| id | INT | PK, AUTO_INCREMENT |
| userId | INT | FK → job_seeker_profile.userAccountId |
| job | INT | FK → post.postId |

**Constraints:**
- UNIQUE(userId, job)

**Relationships:**
- N:1 with JOB_SEEKER_PROFILE
- N:1 with POST

---

### 7. **POST** (post)
| Column | Type | Constraint |
|--------|------|-----------|
| postId | INT | PK, AUTO_INCREMENT |
| jobTitle | VARCHAR | |
| description | VARCHAR(10000) | |
| jobType | VARCHAR | |
| salary | VARCHAR | |
| remote | VARCHAR | |
| postedDate | DATE | |
| recuriterId | INT | FK → users.userId |
| jobLocationId | INT | FK → location.id |
| jobCompanyId | INT | FK → company.id |

**Relationships:**
- N:1 with USERS (recruiter)
- N:1 with LOCATION
- N:1 with COMPANY
- 1:N with SAVED_JOBS
- 1:N with APPLICATION

---

### 8. **APPLICATION** (application)
| Column | Type | Constraint |
|--------|------|-----------|
| id | INT | PK, AUTO_INCREMENT |
| candidate | INT | FK → job_seeker_profile.user_account_id |
| post | INT | FK → post.jobPostId |
| applyDate | DATE | |
| coverLetter | VARCHAR | |

**Constraints:**
- UNIQUE(userId, jobId)

**Relationships:**
- N:1 with JOB_SEEKER_PROFILE
- N:1 with POST

---

### 9. **LOCATION** (location)
| Column | Type | Constraint |
|--------|------|-----------|
| id | INT | PK, AUTO_INCREMENT |
| city | VARCHAR | |
| state | VARCHAR | |
| country | VARCHAR | |

**Relationships:**
- 1:N with POST

---

### 10. **COMPANY** (company)
| Column | Type | Constraint |
|--------|------|-----------|
| id | INT | PK, AUTO_INCREMENT |
| name | VARCHAR | |
| logo | VARCHAR | |

**Relationships:**
- 1:N with POST

---

## Issues Found

### ⚠️ **RecuiterProfile.java - Incorrect Relationship Mapping**

**Problem:**
```java
@OneToMany
@JoinColumn(name="user_account_id")
@MapsId
private Users userId;
```

**Issues:**
1. **`@OneToMany` is incorrect** - A recruiter should have exactly ONE profile (use `@OneToOne`)
2. **`@MapsId` is for `@OneToOne`** - Should not be used with `@OneToMany`
3. **Foreign key logic is backwards** - The relationship should point FROM Users TO RecuiterProfile, not the other way

**Should be:**
```java
@OneToOne
@JoinColumn(name="user_account_id")
@MapsId
private Users user;
```

---

### ⚠️ **Application.java - Column Name Mismatch**

**Problem:**
```java
@JoinColumn(name="job",referencedColumnName = "user_account_id")
private CandidateProfile userId;

@JoinColumn(name="job",referencedColumnName = "jobPostId")
private Post post;
```

**Issue:** Both foreign keys use the same column name `"job"`, which will cause conflicts.

**Should be:**
```java
@JoinColumn(name="user_id",referencedColumnName = "user_account_id")
private CandidateProfile userId;

@JoinColumn(name="job_id",referencedColumnName = "postId")
private Post post;
```

---

## Summary

**Total Entities:** 10  
**Total Relationships:** 15+

**Key Flow:**
- Users register with a UserType (Recruiter or Job Seeker)
- Job Seekers create CandidateProfile + Skills
- Recruiters create RecruiterProfile
- Recruiters post jobs (POST) with Location and Company
- Job Seekers apply for jobs (APPLICATION) and save jobs (SAVED_JOBS)


