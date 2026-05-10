## Entity Relationship Diagram (ERD) - Detailed View

```
┌──────────────────────────────────────────────────────────────────────────────────┐
│                          JOB PORTAL - COMPLETE ERD                              │
└──────────────────────────────────────────────────────────────────────────────────┘


                          ┌────────────────────────┐
                          │     USERS_TYPE         │
                          │  (users_type)          │
                          ├────────────────────────┤
                          │ userTypeId [PK]        │
                          │ userTypeName           │
                          └────────────┬───────────┘
                                       │
                                       │ 1:N
                                       │ (targetEntity=Users)
                                       │ (mappedBy=userTypeId)
                                       │
                     ┌─────────────────▼──────────────────┐
                     │          USERS                     │
                     │        (users)                     │
                     ├────────────────────────────────────┤
                     │ userId [PK]                        │
                     │ email [UNIQUE]                     │
                     │ password [NOT NULL]                │
                     │ isActive                           │
                     │ registrationDate                   │
                     │ usersTypeId [FK] ──────────────┐  │
                     └────────────┬───────────────────┼──┘
                                  │                   │
                         ┌────────┴────────┐          │
                         │                 │          │
                    1:1  │            1:N  │          │
                 (cascade)            (cascade)       │
                         │                 │          │
        ┌────────────────▼───┐  ┌──────────▼──────────────────┐
        │ JOB_SEEKER_PROFILE │  │ RECRUITER_PROFILE            │
        │(job_seeker_profile)│  │ (recruiter_profile)          │
        ├────────────────────┤  ├──────────────────────────────┤
        │ userAccountId[PK,  │  │ userAccountId [PK, FK]       │
        │    FK] (@MapsId)   │  │ firstName                    │
        │ firstName          │  │ lastName                     │
        │ lastName           │  │ city                         │
        │ city               │  │ state                        │
        │ state              │  │ country                      │
        │ country            │  │ company                      │
        │ workAuthorization  │  │ profilePhoto                 │
        │ employmentType     │  │                              │
        │ resume             │  │ ⚠️ NOTE: RecuiterProfile has │
        │ profilePhoto       │  │ @OneToMany with @MapsId      │
        └────────┬───────────┘  │ (INCORRECT - should be       │
                 │              │  @OneToOne)                  │
                 │              └──────────────────────────────┘
                 │
        ┌────────┴─────────────────┬──────────────────┐
        │                          │                  │
       1:N                        1:N               1:N
    (cascade)                  (cascade)       (cascade)
        │                          │                  │
        │                          │                  │
        │                          │          ┌───────▼──────────────┐
        │                          │          │  SAVED_JOBS          │
        │                          │          │  (saved_jobs)        │
        │                          │          ├──────────────────────┤
        │                          │          │ id [PK]              │
        │                          │          │ userId [FK] ────────┐│
        │                          │          │ job [FK] ──────────┐││
        │                          │          │ UNIQUE(userId,job) │││
        │                          │          └──────────────┬─────┼┼┘
        │                          │                        │ N:1 │ N:1
        │                          │                        │     │
        │                          │        ┌───────────────┘     │
        │                          │        │                     │
    ┌────▼──────────────┐  ┌────────▼────────────────────┐        │
    │      SKILLS       │  │     APPLICATION             │        │
    │    (skills)       │  │   (application)             │        │
    ├───────────────────┤  ├─────────────────────────────┤        │
    │ id [PK]           │  │ id [PK]                     │        │
    │ name              │  │ candidate [FK] ────────┐    │        │
    │ experience        │  │ post [FK] ──────────┐  │    │        │
    │ yearsOfExperience │  │ applyDate           │  │    │        │
    │ candidate_profile │  │ coverLetter         │  │    │        │
    │ [FK] ─────────┐   │  │ UNIQUE(candidate,   │  │    │        │
    │               │   │  │   post)             │  │    │        │
    └───────────────┘   │  └──────────┬──────────┼──┼────┼────────┘
                        │             │ N:1     │  │    │
                       │             │         │   │    │
                       │             │         │   │    │
                       │             │    ┌────▼───▼─┐  │
                       │             │    │  POST    │  │
                       │             │    │ (post)   │  │
                       │             │    ├──────────┼──┼─────────────┐
                       │             └───▶│ postId   │  │             │
                       │                  │ [PK]     │  │             │
                       │                  │ jobTitle │◀─┴─────────────┘
                       │                  │ description
                       │                  │ jobType
                       │                  │ salary
                       │                  │ remote
                       │                  │ postedDate
                       │                  │ recuriterId[FK] ──────┐
                       │                  │ jobLocationId[FK]     │
                       │                  │ jobCompanyId[FK]      │
                       │                  │ isActive(Transient)   │
                       │                  │ isSaved(Transient)    │
                       │                  └──────────┬─┬──────────┘
                       │                             │ │
                       │                    ┌────────┘ │
                       │                    │          │
                       │              N:1   │          │ N:1
                       │            (cascade)         (cascade)
                       │              │                 │
                       │              │                 │
        ┌──────────────┴──────┐  ┌────▼─────────────────▼─────────┐
        │   LOCATION          │  │   COMPANY                      │
        │  (location)         │  │  (company)                     │
        ├─────────────────────┤  ├────────────────────────────────┤
        │ id [PK]             │  │ id [PK]                        │
        │ city                │  │ name                           │
        │ state               │  │ logo                           │
        │ country             │  │                                │
        └─────────────────────┘  └────────────────────────────────┘


```

---

## Relationship Summary

| From | To | Type | Cardinality | Note |
|------|-----|------|-------------|------|
| USERS_TYPE | USERS | One-to-Many | 1:N | User can have type (Recruiter/JobSeeker) |
| USERS | JOB_SEEKER_PROFILE | One-to-One | 1:1 | User has at most one job seeker profile |
| USERS | RECRUITER_PROFILE | One-to-One | 1:1 | User has at most one recruiter profile |
| JOB_SEEKER_PROFILE | SKILLS | One-to-Many | 1:N | Candidate can have multiple skills |
| JOB_SEEKER_PROFILE | SAVED_JOBS | One-to-Many | 1:N | Candidate can save multiple jobs |
| JOB_SEEKER_PROFILE | APPLICATION | One-to-Many | 1:N | Candidate can apply to multiple jobs |
| USERS | POST | One-to-Many | 1:N | Recruiter can post multiple jobs |
| POST | SAVED_JOBS | One-to-Many | 1:N | Job can be saved by multiple candidates |
| POST | APPLICATION | One-to-Many | 1:N | Job can receive multiple applications |
| LOCATION | POST | One-to-Many | 1:N | Location can have multiple jobs |
| COMPANY | POST | One-to-Many | 1:N | Company can have multiple jobs |

---

## Cascade Rules Applied

| Relationship | Cascade Type | Impact |
|-------------|------|--------|
| USERS_TYPE → USERS | CascadeType.ALL | Deleting UserType cascades to Users |
| USERS → JOB_SEEKER_PROFILE | CascadeType.NONE | Explicit delete needed |
| USERS → RECRUITER_PROFILE | CascadeType.NONE | Explicit delete needed |
| JOB_SEEKER_PROFILE → SKILLS | CascadeType.ALL | Deleting candidate deletes skills |
| JOB_SEEKER_PROFILE → SAVED_JOBS | CascadeType.ALL | Deleting candidate deletes saved jobs |
| JOB_SEEKER_PROFILE → APPLICATION | CascadeType.ALL | Deleting candidate deletes applications |
| USERS → POST | No Cascade | Deleting recruiter doesn't delete jobs |
| POST → SAVED_JOBS | CascadeType.ALL | Deleting job deletes saved records |
| POST → APPLICATION | CascadeType.ALL | Deleting job deletes applications |
| LOCATION → POST | CascadeType.ALL | Deleting location deletes jobs (risky!) |
| COMPANY → POST | CascadeType.ALL | Deleting company deletes jobs (risky!) |

---

## Data Flow

### User Registration Flow
```
Registration
    ↓
Create USERS record
    ↓
Select UserType (Recruiter or JobSeeker)
    ↓
Create RECRUITER_PROFILE (if Recruiter)
or
Create JOB_SEEKER_PROFILE (if JobSeeker)
```

### Job Posting Flow
```
Recruiter logs in
    ↓
Creates POST with:
  - Job title, description
  - Associate LOCATION
  - Associate COMPANY
    ↓
Job is now visible to Job Seekers
```

### Job Application Flow
```
Job Seeker searches for jobs
    ↓
Views POST details
    ↓
Option 1: Save job → Create SAVED_JOBS record
Option 2: Apply → Create APPLICATION record + SAVED_JOBS (optional)
```

### Job Seeker Profile Flow
```
Job Seeker logs in
    ↓
Updates JOB_SEEKER_PROFILE:
  - Personal info
  - Work Authorization
  - Employment type
  - Resume
    ↓
Add SKILLS:
  - Skill name
  - Experience level
  - Years of experience
```

---

## Key Constraints

### Unique Constraints
1. **USERS.email** - One email per user
2. **SAVED_JOBS(userId, job)** - Can't save same job twice
3. **APPLICATION(userId, job)** - Can't apply to same job twice

### Foreign Key Constraints
- All ForeignKeys have `ON DELETE CASCADE`
- This means deleting parent automatically deletes children

### NotNull Constraints
- **USERS.password** - Required
- **USERS.usersTypeId** - Every user must have a type
- **POST.recuriterId, jobLocationId, jobCompanyId** - Job must have recruiter, location, company
- **SAVED_JOBS.userId, job** - Both references required
- **APPLICATION.userId, job** - Both references required

---

## Known Issues

### ⚠️ Issue 1: RecuiterProfile.java
**Location:** `RecuiterProfile.java`
**Problem:** Uses `@OneToMany` with `@MapsId` instead of `@OneToOne`
```java
// WRONG:
@OneToMany
@JoinColumn(name="user_account_id")
@MapsId
private Users userId;

// CORRECT:
@OneToOne
@JoinColumn(name="user_account_id")
@MapsId
private Users userId;
```

### ⚠️ Issue 2: Application.java - RESOLVED ✅
**Location:** `Application.java`
**Status:** Fixed - Column names are now distinct
```java
// FIXED:
@JoinColumn(name="candidate",referencedColumnName = "user_account_id")
private CandidateProfile userId;

@JoinColumn(name="post",referencedColumnName = "jobPostId")
private Post post;
```

**Note:** The column names are now `candidate` and `post` instead of both being `job`.

### ⚠️ Issue 3: Cascade DELETE Risks
**Location:** Multiple entities
**Problem:** Using `CascadeType.ALL` on LOCATION and COMPANY
- Deleting a location cascades to all jobs in that location
- Deleting a company cascades to all jobs in that company
- This might not be desired behavior

**Recommendation:** Use `CascadeType.PERSIST` only, not `ALL`

---

## Database Table Count: 10

1. users_type
2. users
3. job_seeker_profile
4. recruiter_profile
5. location
6. company
7. post
8. skills
9. saved_jobs
10. application



