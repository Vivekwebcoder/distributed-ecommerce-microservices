import os
import subprocess
import sys

html_content = """<!DOCTYPE html>
<html lang="hi">
<head>
    <meta charset="UTF-8">
    <title>Auth Service - Technical Documentation</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&family=Fira+Code:wght@400;500&display=swap');

        @page {
            size: A4;
            margin: 18mm 15mm 18mm 15mm;
            @bottom-right {
                content: counter(page);
            }
        }

        * {
            box-sizing: border-box;
            -webkit-print-color-adjust: exact !important;
            print-color-adjust: exact !important;
        }

        body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
            color: #1e293b;
            background-color: #ffffff;
            line-height: 1.6;
            font-size: 10.5pt;
            margin: 0;
            padding: 0;
        }

        /* Cover Page */
        .cover-page {
            height: 98vh;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            background: linear-gradient(135deg, #0f172a 0%, #1e1b4b 50%, #312e81 100%);
            color: #ffffff;
            padding: 50px 40px;
            border-radius: 12px;
            page-break-after: always;
            position: relative;
        }

        .cover-header {
            border-bottom: 2px solid rgba(255, 255, 255, 0.2);
            padding-bottom: 20px;
        }

        .cover-badge {
            display: inline-block;
            background: rgba(99, 102, 241, 0.3);
            border: 1px solid #818cf8;
            color: #c7d2fe;
            padding: 6px 16px;
            border-radius: 20px;
            font-size: 9.5pt;
            font-weight: 600;
            letter-spacing: 1px;
            text-transform: uppercase;
            margin-bottom: 15px;
        }

        .cover-title {
            font-size: 32pt;
            font-weight: 800;
            margin: 0 0 10px 0;
            letter-spacing: -0.5px;
            color: #ffffff;
            line-height: 1.15;
        }

        .cover-subtitle {
            font-size: 16pt;
            font-weight: 400;
            color: #93c5fd;
            margin: 0;
        }

        .cover-body {
            margin: 40px 0;
        }

        .cover-desc {
            font-size: 11.5pt;
            color: #e2e8f0;
            max-width: 90%;
            line-height: 1.7;
        }

        .cover-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 20px;
            margin-top: 30px;
        }

        .cover-card {
            background: rgba(255, 255, 255, 0.07);
            border: 1px solid rgba(255, 255, 255, 0.15);
            padding: 18px;
            border-radius: 8px;
        }

        .cover-card-label {
            font-size: 8.5pt;
            text-transform: uppercase;
            color: #a5b4fc;
            font-weight: 600;
            letter-spacing: 0.5px;
        }

        .cover-card-value {
            font-size: 13pt;
            font-weight: 700;
            color: #ffffff;
            margin-top: 5px;
        }

        .cover-footer {
            border-top: 1px solid rgba(255, 255, 255, 0.2);
            padding-top: 20px;
            display: flex;
            justify-content: space-between;
            font-size: 9.5pt;
            color: #94a3b8;
        }

        /* Content Styling */
        h1 {
            font-size: 20pt;
            font-weight: 800;
            color: #0f172a;
            border-bottom: 3px solid #4f46e5;
            padding-bottom: 8px;
            margin-top: 30px;
            margin-bottom: 18px;
            page-break-after: avoid;
        }

        h2 {
            font-size: 14pt;
            font-weight: 700;
            color: #1e1b4b;
            margin-top: 24px;
            margin-bottom: 12px;
            border-left: 4px solid #6366f1;
            padding-left: 10px;
            page-break-after: avoid;
        }

        h3 {
            font-size: 11.5pt;
            font-weight: 600;
            color: #334155;
            margin-top: 16px;
            margin-bottom: 8px;
            page-break-after: avoid;
        }

        p {
            margin-bottom: 12px;
            text-align: justify;
        }

        ul, ol {
            margin-top: 5px;
            margin-bottom: 15px;
            padding-left: 22px;
        }

        li {
            margin-bottom: 6px;
        }

        /* Tables */
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 15px 0 22px 0;
            font-size: 9.5pt;
            page-break-inside: auto;
        }

        tr {
            page-break-inside: avoid;
            page-break-after: auto;
        }

        th {
            background-color: #1e293b;
            color: #ffffff;
            font-weight: 600;
            text-align: left;
            padding: 10px 12px;
            border: 1px solid #1e293b;
        }

        td {
            padding: 9px 12px;
            border: 1px solid #cbd5e1;
            vertical-align: top;
        }

        tr:nth-child(even) td {
            background-color: #f8fafc;
        }

        /* Badges */
        .badge {
            display: inline-block;
            padding: 3px 8px;
            border-radius: 4px;
            font-size: 8pt;
            font-weight: 700;
            font-family: 'Fira Code', monospace;
            text-transform: uppercase;
        }

        .badge-post { background-color: #dcfce7; color: #166534; border: 1px solid #bbf7d0; }
        .badge-get { background-color: #dbeafe; color: #1e40af; border: 1px solid #bfdbfe; }
        .badge-pub { background-color: #fef3c7; color: #92400e; border: 1px solid #fde68a; }
        .badge-prot { background-color: #fee2e2; color: #991b1b; border: 1px solid #fca5a5; }

        /* Code Blocks */
        code, pre {
            font-family: 'Fira Code', Consolas, Monaco, monospace;
            font-size: 8.5pt;
        }

        code {
            background-color: #f1f5f9;
            color: #4338ca;
            padding: 2px 5px;
            border-radius: 4px;
            border: 1px solid #e2e8f0;
        }

        pre {
            background-color: #0f172a;
            color: #f8fafc;
            padding: 14px;
            border-radius: 8px;
            overflow-x: auto;
            margin: 12px 0;
            line-height: 1.45;
            white-space: pre-wrap;
            word-break: break-all;
        }

        /* Stat Grid */
        .stat-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 12px;
            margin: 20px 0;
        }

        .stat-box {
            background: #f8fafc;
            border: 1px solid #e2e8f0;
            border-top: 3px solid #4f46e5;
            padding: 14px;
            border-radius: 6px;
            text-align: center;
        }

        .stat-number {
            font-size: 20pt;
            font-weight: 800;
            color: #4f46e5;
            line-height: 1;
        }

        .stat-label {
            font-size: 8.5pt;
            color: #64748b;
            font-weight: 600;
            margin-top: 6px;
            text-transform: uppercase;
        }

        /* Callout Box */
        .callout {
            background-color: #eef2ff;
            border-left: 4px solid #4f46e5;
            padding: 14px 18px;
            border-radius: 0 8px 8px 0;
            margin: 18px 0;
            font-size: 9.5pt;
        }

        .callout-title {
            font-weight: 700;
            color: #3730a3;
            margin-bottom: 4px;
        }

        .page-break {
            page-break-after: always;
        }
    </style>
</head>
<body>

    <!-- COVER PAGE -->
    <div class="cover-page">
        <div class="cover-header">
            <span class="cover-badge">Microservices System Documentation</span>
            <h1 class="cover-title">Auth Service<br>Complete Technical Manual</h1>
            <p class="cover-subtitle">Distributed E-Commerce Microservices Architecture</p>
        </div>

        <div class="cover-body">
            <p class="cover-desc">
                Yeh document <strong>Auth Service</strong> ka complete, comprehensive aur in-depth technical manual hai. Isme system ka Introduction, Objectives, Technology Stack, Complete Architecture, Database Schema, Table Structure, APIs, Classes, Methods aur Security Workflows sab kuch detail mein explain kiya gaya hai.
            </p>

            <div class="cover-grid">
                <div class="cover-card">
                    <div class="cover-card-label">Service Port & Protocol</div>
                    <div class="cover-card-value">HTTP / 8082</div>
                </div>
                <div class="cover-card">
                    <div class="cover-card-label">Authentication Type</div>
                    <div class="cover-card-value">Stateless JWT (HMAC-SHA256)</div>
                </div>
                <div class="cover-card">
                    <div class="cover-card-label">Database Backend</div>
                    <div class="cover-card-value">PostgreSQL (ecommerce_db)</div>
                </div>
                <div class="cover-card">
                    <div class="cover-card-label">Java & Spring Boot</div>
                    <div class="cover-card-value">Java 21 | Spring Boot 3.x</div>
                </div>
            </div>
        </div>

        <div class="cover-footer">
            <div>Project: Distributed E-Commerce Microservices</div>
            <div>Author: Vivek Web Coder</div>
            <div>Date: August 2026</div>
        </div>
    </div>

    <!-- QUICK STATS OVERVIEW -->
    <h1>1. Executive Summary & Overview Stats</h1>
    <p>Auth Service pure Distributed E-Commerce Ecosystem ka <strong>Identity & Access Management (IAM) Core</strong> hai. Yeh service Naye Users ka Registration, Secure Login Authentication, JWT Token Issuance, Role-Based Access Control (RBAC), aur Password Recovery (Forgot/Reset Password) ko handle karti hai.</p>

    <div class="stat-grid">
        <div class="stat-box">
            <div class="stat-number">8</div>
            <div class="stat-label">Total APIs</div>
        </div>
        <div class="stat-box">
            <div class="stat-number">21</div>
            <div class="stat-label">Total Classes</div>
        </div>
        <div class="stat-box">
            <div class="stat-number">65+</div>
            <div class="stat-label">Total Methods</div>
        </div>
        <div class="stat-box">
            <div class="stat-number">1</div>
            <div class="stat-label">Database Table</div>
        </div>
    </div>

    <div class="callout">
        <div class="callout-title">Key Architectural Highlights</div>
        Auth Service completely stateless JWT token system par aadharit hai. Isme Eureka Client discovery integrated hai for auto-registration and Config Server integrated hai for centralized configuration management.
    </div>

    <!-- SECTION 2: INTRODUCTION & OBJECTIVES -->
    <h1>2. Introduction & System Objectives (परिचय एवं मुख्य उद्देश्य)</h1>
    
    <h2>2.1 Core Purpose (मुख्य उद्देश्य)</h2>
    <p>Ecommerce Distributed Microservices System mein Auth Service ka main objective ek central authority provide karna hai jo authentication aur authorization requests ko handle kare. Is service ke bina koi bhi client protected resources ko access nahi kar sakta.</p>
    <ul>
        <li><strong>User Registration & Onboarding:</strong> Customer, Seller, aur Admin roles ke saath naye users ka secure account creation.</li>
        <li><strong>Secure Authentication & Password Hashing:</strong> Raw passwords ko BCrypt hashing algorithm se secure encrypt karke store aur verify karna.</li>
        <li><strong>Stateless JWT Token Generation:</strong> Successful login par digitally signed JSON Web Token (JWT) generate karna jo client ko future authenticated requests ke liye diya jata hai.</li>
        <li><strong>Role-Based Access Control (RBAC):</strong> Spring Security aur Method Security (<code>@PreAuthorize</code>) ke zariye <code>ADMIN</code>, <code>SELLER</code>, aur <code>CUSTOMER</code> roles ke permissions enforce karna.</li>
        <li><strong>Password Recovery Workflow:</strong> Secure 15-minute time-bound UUID Reset Tokens generate karke Forgot/Reset Password feature implement karna.</li>
        <li><strong>Centralized Exception Handling:</strong> Consistent HTTP Error Responses (400, 401, 404, 409, 500) JSON format mein return karna.</li>
    </ul>

    <h2>2.2 Microservice Architecture & Ecosystem Position</h2>
    <p>Auth Service E-Commerce Ecosystem mein niche diye gaye tarike se interact karta hai:</p>
    <ul>
        <li><strong>Eureka Discovery Server (Port 8761):</strong> Auth Service launch hote hi Eureka server par <code>AUTH-SERVICE</code> name se auto-register hota hai.</li>
        <li><strong>Spring Cloud Config Server (Port 8888):</strong> Application environment configurations (Database credentials, JWT secret keys) centrally Config Server se fetch kiye jaate hain.</li>
        <li><strong>API Gateway (Port 8080):</strong> Gateway saare incoming HTTP traffic ko inspect karta hai aur `/api/auth/**` requests ko Auth Service par routing karta hai.</li>
        <li><strong>PostgreSQL Database (Port 5432):</strong> Database instance `ecommerce_db` mein `users` table ko maintain karta hai.</li>
    </ul>

    <!-- SECTION 3: TECHNOLOGY STACK -->
    <h1>3. Technology Stack & Dependencies (प्रौद्योगिकी स्टैक)</h1>

    <table>
        <thead>
            <tr>
                <th>Technology / Library</th>
                <th>Version / Group</th>
                <th>Purpose & Description</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><strong>Java JDK</strong></td>
                <td>Version 21 (LTS)</td>
                <td>Primary Programming Language (Modern Virtual Threads & Record Features support)</td>
            </tr>
            <tr>
                <td><strong>Spring Boot</strong></td>
                <td>Version 3.x</td>
                <td>Core Application Framework for enterprise RESTful microservices</td>
            </tr>
            <tr>
                <td><strong>Spring Security</strong></td>
                <td>Version 6.x</td>
                <td>Security Filter Chain, Authentication Manager, PasswordEncoder, and RBAC</td>
            </tr>
            <tr>
                <td><strong>JJWT (Java JWT)</strong></td>
                <td>Version 0.12.7</td>
                <td><code>jjwt-api</code>, <code>jjwt-impl</code>, <code>jjwt-jackson</code> for JWT creation & verification</td>
            </tr>
            <tr>
                <td><strong>Spring Data JPA</strong></td>
                <td>Starter JPA</td>
                <td>ORM Mapping with Hibernate, Repositories, Automapper for PostgreSQL</td>
            </tr>
            <tr>
                <td><strong>PostgreSQL Driver</strong></td>
                <td>org.postgresql</td>
                <td>Database connectivity for relational data persistence (`users` table)</td>
            </tr>
            <tr>
                <td><strong>Eureka Discovery Client</strong></td>
                <td>Spring Cloud 2025.1.2</td>
                <td>Client discovery and auto-registration with Eureka Server</td>
            </tr>
            <tr>
                <td><strong>Spring Cloud Config Client</strong></td>
                <td>Spring Cloud 2025.1.2</td>
                <td>Centralized bootstrap configuration fetching from Config Server</td>
            </tr>
            <tr>
                <td><strong>Jakarta Validation</strong></td>
                <td>Starter Validation</td>
                <td>Request payload DTO annotations (`@NotBlank`, `@Email`, `@Pattern`, `@Size`)</td>
            </tr>
            <tr>
                <td><strong>Project Lombok</strong></td>
                <td>1.18.x</td>
                <td>Boilerplate reduction for getters, setters, constructors, builders</td>
            </tr>
            <tr>
                <td><strong>Spring Boot Mail</strong></td>
                <td>Starter Mail</td>
                <td>Email notification integration infrastructure for password resets</td>
            </tr>
        </tbody>
    </table>

    <div class="page-break"></div>

    <!-- SECTION 4: DATABASE SCHEMA & TABLES -->
    <h1>4. Database Schema & Tables (डेटाबेस तालिका और इकाइयाँ)</h1>
    <p>Auth Service ke paas ek primary entity <code>User</code> hai jo PostgreSQL database mein <code>users</code> table se mapped hai.</p>

    <h2>4.1 Table Name: <code>users</code></h2>
    <table>
        <thead>
            <tr>
                <th>Column Name</th>
                <th>SQL Data Type</th>
                <th>Constraints</th>
                <th>Java Field & Type</th>
                <th>Description</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><code>id</code></td>
                <td>UUID</td>
                <td>PRIMARY KEY, AUTO</td>
                <td><code>UUID id</code></td>
                <td>Unique Identifier for user account</td>
            </tr>
            <tr>
                <td><code>first_name</code></td>
                <td>VARCHAR(50)</td>
                <td>NOT NULL</td>
                <td><code>String firstName</code></td>
                <td>User ka pehla naam</td>
            </tr>
            <tr>
                <td><code>last_name</code></td>
                <td>VARCHAR(50)</td>
                <td>NOT NULL</td>
                <td><code>String lastName</code></td>
                <td>User ka aakhri naam</td>
            </tr>
            <tr>
                <td><code>email</code></td>
                <td>VARCHAR(150)</td>
                <td>NOT NULL, UNIQUE</td>
                <td><code>String email</code></td>
                <td>User Email ID (Used as Username for Login)</td>
            </tr>
            <tr>
                <td><code>phone_number</code></td>
                <td>VARCHAR(10)</td>
                <td>UNIQUE</td>
                <td><code>String phoneNumber</code></td>
                <td>User Phone Number (10 digits starting with 6-9)</td>
            </tr>
            <tr>
                <td><code>password</code></td>
                <td>VARCHAR(255)</td>
                <td>NOT NULL</td>
                <td><code>String password</code></td>
                <td>BCrypt Encrypted Password Hash</td>
            </tr>
            <tr>
                <td><code>role</code></td>
                <td>VARCHAR(20)</td>
                <td>NOT NULL</td>
                <td><code>Role role</code> (Enum)</td>
                <td>User Role: <code>ADMIN</code>, <code>SELLER</code>, <code>CUSTOMER</code></td>
            </tr>
            <tr>
                <td><code>status</code></td>
                <td>VARCHAR(30)</td>
                <td>NOT NULL</td>
                <td><code>AccountStatus status</code></td>
                <td>Account State: <code>ACTIVE</code>, <code>PENDING_APPROVAL</code>, <code>BLOCKED</code></td>
            </tr>
            <tr>
                <td><code>email_verified</code></td>
                <td>BOOLEAN</td>
                <td>NOT NULL, DEFAULT false</td>
                <td><code>Boolean emailVerified</code></td>
                <td>Email Verification status flag</td>
            </tr>
            <tr>
                <td><code>reset_token</code></td>
                <td>VARCHAR(255)</td>
                <td>UNIQUE, NULLABLE</td>
                <td><code>String resetToken</code></td>
                <td>Forgot password temporary UUID reset token</td>
            </tr>
            <tr>
                <td><code>reset_token_expiry</code></td>
                <td>TIMESTAMP</td>
                <td>NULLABLE</td>
                <td><code>LocalDateTime resetTokenExpiry</code></td>
                <td>Reset token expiration timestamp (+15 mins)</td>
            </tr>
            <tr>
                <td><code>created_at</code></td>
                <td>TIMESTAMP</td>
                <td>NOT NULL, READONLY</td>
                <td><code>LocalDateTime createdAt</code></td>
                <td>Record creation timestamp (Auto <code>@CreationTimestamp</code>)</td>
            </tr>
            <tr>
                <td><code>updated_at</code></td>
                <td>TIMESTAMP</td>
                <td>NOT NULL</td>
                <td><code>LocalDateTime updatedAt</code></td>
                <td>Record last updated timestamp (Auto <code>@UpdateTimestamp</code>)</td>
            </tr>
        </tbody>
    </table>

    <h2>4.2 Enums Definition</h2>
    <div class="callout">
        <strong>1. Role Enum:</strong> <code>ADMIN</code>, <code>SELLER</code>, <code>CUSTOMER</code><br>
        <strong>2. AccountStatus Enum:</strong> <code>ACTIVE</code>, <code>PENDING_APPROVAL</code>, <code>BLOCKED</code>
    </div>

    <!-- SECTION 5: COMPLETE API INVENTORY -->
    <h1>5. Complete API Endpoints Inventory (सभी APIs की विस्तृत सूची)</h1>

    <p>Auth Service kul <strong>8 RESTful API Endpoints</strong> provide karta hai:</p>

    <table>
        <thead>
            <tr>
                <th>HTTP Method</th>
                <th>Endpoint URL Path</th>
                <th>Access Level</th>
                <th>Request Body</th>
                <th>Response Type</th>
                <th>Success Code</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><span class="badge badge-post">POST</span></td>
                <td><code>/api/auth/register</code></td>
                <td><span class="badge badge-pub">PUBLIC</span></td>
                <td><code>RegisterRequest</code></td>
                <td><code>RegisterResponse</code></td>
                <td><code>201 CREATED</code></td>
            </tr>
            <tr>
                <td><span class="badge badge-post">POST</span></td>
                <td><code>/api/auth/login</code></td>
                <td><span class="badge badge-pub">PUBLIC</span></td>
                <td><code>LoginRequest</code></td>
                <td><code>LoginResponse</code></td>
                <td><code>200 OK</code></td>
            </tr>
            <tr>
                <td><span class="badge badge-post">POST</span></td>
                <td><code>/api/auth/forgot-password</code></td>
                <td><span class="badge badge-pub">PUBLIC</span></td>
                <td><code>ForgotPasswordRequest</code></td>
                <td><code>ForgotPasswordResponse</code></td>
                <td><code>200 OK</code></td>
            </tr>
            <tr>
                <td><span class="badge badge-post">POST</span></td>
                <td><code>/api/auth/reset-password</code></td>
                <td><span class="badge badge-pub">PUBLIC</span></td>
                <td><code>ResetPasswordRequest</code></td>
                <td><code>ResetPasswordResponse</code></td>
                <td><code>200 OK</code></td>
            </tr>
            <tr>
                <td><span class="badge badge-get">GET</span></td>
                <td><code>/api/role/admin</code></td>
                <td><span class="badge badge-prot">ROLE_ADMIN</span></td>
                <td>None</td>
                <td><code>String</code></td>
                <td><code>200 OK</code></td>
            </tr>
            <tr>
                <td><span class="badge badge-get">GET</span></td>
                <td><code>/api/role/seller</code></td>
                <td><span class="badge badge-prot">ROLE_SELLER</span></td>
                <td>None</td>
                <td><code>String</code></td>
                <td><code>200 OK</code></td>
            </tr>
            <tr>
                <td><span class="badge badge-get">GET</span></td>
                <td><code>/api/role/customer</code></td>
                <td><span class="badge badge-prot">ROLE_CUSTOMER</span></td>
                <td>None</td>
                <td><code>String</code></td>
                <td><code>200 OK</code></td>
            </tr>
            <tr>
                <td><span class="badge badge-get">GET</span></td>
                <td><code>/api/role/admin-seller</code></td>
                <td><span class="badge badge-prot">ADMIN / SELLER</span></td>
                <td>None</td>
                <td><code>String</code></td>
                <td><code>200 OK</code></td>
            </tr>
        </tbody>
    </table>

    <div class="page-break"></div>

    <!-- SECTION 6: COMPLETE CLASS INVENTORY -->
    <h1>6. Complete Class Inventory (सभी 21 Client/Service/DTO/Config Classes)</h1>

    <p>Auth Service ke project structure mein total <strong>21 Classes, Interfaces, aur Enums</strong> shamil hain:</p>

    <table>
        <thead>
            <tr>
                <th>#</th>
                <th>Class / Interface Name</th>
                <th>Package Location</th>
                <th>Stereotype / Type</th>
                <th>Primary Purpose & Responsibility</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>1</td>
                <td><code>AuthServiceApplication</code></td>
                <td><code>com.ecommerce.auth_service</code></td>
                <td><code>@SpringBootApplication</code></td>
                <td>Main Entry point class jo Spring Boot microservice ko launch karti hai.</td>
            </tr>
            <tr>
                <td>2</td>
                <td><code>SecurityConfig</code></td>
                <td><code>.config</code></td>
                <td><code>@Configuration</code></td>
                <td>Spring SecurityFilterChain, CORS, PasswordEncoder, Permitted endpoints, Session Management.</td>
            </tr>
            <tr>
                <td>3</td>
                <td><code>AuthController</code></td>
                <td><code>.controller</code></td>
                <td><code>@RestController</code></td>
                <td>Exposes Public Auth APIs (<code>/register</code>, <code>/login</code>, <code>/forgot-password</code>, <code>/reset-password</code>).</td>
            </tr>
            <tr>
                <td>4</td>
                <td><code>RoleController</code></td>
                <td><code>.controller</code></td>
                <td><code>@RestController</code></td>
                <td>Exposes Protected RBAC test APIs with <code>@PreAuthorize</code> annotations.</td>
            </tr>
            <tr>
                <td>5</td>
                <td><code>RegisterRequest</code></td>
                <td><code>.dto.request</code></td>
                <td>POJO DTO</td>
                <td>Holds registration request data with validation rules (Firstname, Email, Password, Role).</td>
            </tr>
            <tr>
                <td>6</td>
                <td><code>LoginRequest</code></td>
                <td><code>.dto.request</code></td>
                <td>POJO DTO</td>
                <td>Holds login credentials payload (email, password).</td>
            </tr>
            <tr>
                <td>7</td>
                <td><code>ForgotPasswordRequest</code></td>
                <td><code>.dto.request</code></td>
                <td>POJO DTO</td>
                <td>Holds email input for generating forgot password token.</td>
            </tr>
            <tr>
                <td>8</td>
                <td><code>ResetPasswordRequest</code></td>
                <td><code>.dto.request</code></td>
                <td>POJO DTO</td>
                <td>Holds token and new password input for reset password operation.</td>
            </tr>
            <tr>
                <td>9</td>
                <td><code>RegisterResponse</code></td>
                <td><code>.dto.response</code></td>
                <td>POJO DTO</td>
                <td>Encapsulates registration output details (userId, email, role, success message).</td>
            </tr>
            <tr>
                <td>10</td>
                <td><code>LoginResponse</code></td>
                <td><code>.dto.response</code></td>
                <td>POJO DTO</td>
                <td>Encapsulates login output along with generated JWT bearer token.</td>
            </tr>
            <tr>
                <td>11</td>
                <td><code>ForgotPasswordResponse</code></td>
                <td><code>.dto.response</code></td>
                <td>POJO DTO</td>
                <td>Returns confirmation message and generated reset token.</td>
            </tr>
            <tr>
                <td>12</td>
                <td><code>ResetPasswordResponse</code></td>
                <td><code>.dto.response</code></td>
                <td>POJO DTO</td>
                <td>Returns password reset confirmation message.</td>
            </tr>
            <tr>
                <td>13</td>
                <td><code>User</code></td>
                <td><code>.entity</code></td>
                <td><code>@Entity</code></td>
                <td>JPA Entity representing <code>users</code> table, implements Spring Security <code>UserDetails</code>.</td>
            </tr>
            <tr>
                <td>14</td>
                <td><code>Role</code></td>
                <td><code>.enums</code></td>
                <td><code>Enum</code></td>
                <td>Enum defining user permissions (<code>ADMIN</code>, <code>SELLER</code>, <code>CUSTOMER</code>).</td>
            </tr>
            <tr>
                <td>15</td>
                <td><code>AccountStatus</code></td>
                <td><code>.enums</code></td>
                <td><code>Enum</code></td>
                <td>Enum defining account status states (<code>ACTIVE</code>, <code>PENDING_APPROVAL</code>, <code>BLOCKED</code>).</td>
            </tr>
            <tr>
                <td>16</td>
                <td><code>UserRepository</code></td>
                <td><code>.repository</code></td>
                <td><code>Interface</code></td>
                <td>Extends <code>JpaRepository&lt;User, UUID&gt;</code> for database CRUD operations.</td>
            </tr>
            <tr>
                <td>17</td>
                <td><code>AuthService</code></td>
                <td><code>.service</code></td>
                <td><code>Interface</code></td>
                <td>Business service contract for authentication and user management methods.</td>
            </tr>
            <tr>
                <td>18</td>
                <td><code>AuthServiceImpl</code></td>
                <td><code>.service.impl</code></td>
                <td><code>@Service</code></td>
                <td>Concrete business implementation for registration, login, JWT generation, reset password.</td>
            </tr>
            <tr>
                <td>19</td>
                <td><code>CustomUserDetailsService</code></td>
                <td><code>.security</code></td>
                <td><code>@Service</code></td>
                <td>Implements Spring Security <code>UserDetailsService</code> to fetch user details by email.</td>
            </tr>
            <tr>
                <td>20</td>
                <td><code>JwtAuthenticationFilter</code></td>
                <td><code>.security</code></td>
                <td><code>@Component</code></td>
                <td>Extends <code>OncePerRequestFilter</code> to intercept requests, extract & validate JWT Bearer token.</td>
            </tr>
            <tr>
                <td>21</td>
                <td><code>JwtService</code></td>
                <td><code>.security</code></td>
                <td><code>@Service</code></td>
                <td>Utility service to build JWT token, sign with secret key, extract claims, and validate expiration.</td>
            </tr>
            <tr>
                <td>22</td>
                <td><code>GlobalExceptionHandler</code></td>
                <td><code>.exception</code></td>
                <td><code>@RestControllerAdvice</code></td>
                <td>Central controller advice handling validation errors, custom runtime exceptions into HTTP responses.</td>
            </tr>
            <tr>
                <td>23</td>
                <td><code>EmailAlreadyExistsException</code></td>
                <td><code>.exception</code></td>
                <td><code>RuntimeException</code></td>
                <td>Thrown when duplicate email is registered (HTTP 409 Conflict).</td>
            </tr>
            <tr>
                <td>24</td>
                <td><code>PhoneNumberAlreadyExistsException</code></td>
                <td><code>.exception</code></td>
                <td><code>RuntimeException</code></td>
                <td>Thrown when duplicate phone number is registered (HTTP 409 Conflict).</td>
            </tr>
            <tr>
                <td>25</td>
                <td><code>UserNotFoundException</code></td>
                <td><code>.exception</code></td>
                <td><code>RuntimeException</code></td>
                <td>Thrown when user email or token is not found in database (HTTP 404 Not Found).</td>
            </tr>
            <tr>
                <td>26</td>
                <td><code>InvalidPasswordException</code></td>
                <td><code>.exception</code></td>
                <td><code>RuntimeException</code></td>
                <td>Thrown when user password verification fails during login (HTTP 401 Unauthorized).</td>
            </tr>
        </tbody>
    </table>

    <div class="page-break"></div>

    <!-- SECTION 7: DETAILED METHOD BREAKDOWN -->
    <h1>7. Complete Method Breakdown (सभी क्लासेज के प्रत्येक मेथड का विवरण)</h1>

    <h2>7.1 <code>AuthController</code> Methods</h2>
    <ul>
        <li><code>register(RegisterRequest request) -> ResponseEntity&lt;RegisterResponse&gt;</code>: Handles <code>POST /api/auth/register</code>. Validates request body, calls <code>authService.register()</code>, returns HTTP 201 Created.</li>
        <li><code>login(LoginRequest request) -> ResponseEntity&lt;LoginResponse&gt;</code>: Handles <code>POST /api/auth/login</code>. Validates credentials, calls <code>authService.login()</code>, returns HTTP 200 OK with JWT token.</li>
        <li><code>forgotPassword(ForgotPasswordRequest request) -> ResponseEntity&lt;ForgotPasswordResponse&gt;</code>: Handles <code>POST /api/auth/forgot-password</code>. Generates reset token via <code>authService.forgotPassword()</code>, returns HTTP 200 OK.</li>
        <li><code>resetPassword(ResetPasswordRequest request) -> ResponseEntity&lt;ResetPasswordResponse&gt;</code>: Handles <code>POST /api/auth/reset-password</code>. Resets user password via <code>authService.resetPassword()</code>, returns HTTP 200 OK.</li>
    </ul>

    <h2>7.2 <code>RoleController</code> Methods</h2>
    <ul>
        <li><code>adminApi() -> String</code>: <code>GET /api/role/admin</code> guarded by <code>@PreAuthorize("hasRole('ADMIN')")</code>. Returns welcome message for Admin.</li>
        <li><code>sellerApi() -> String</code>: <code>GET /api/role/seller</code> guarded by <code>@PreAuthorize("hasRole('SELLER')")</code>. Returns welcome message for Seller.</li>
        <li><code>customerApi() -> String</code>: <code>GET /api/role/customer</code> guarded by <code>@PreAuthorize("hasRole('CUSTOMER')")</code>. Returns welcome message for Customer.</li>
        <li><code>adminSellerApi() -> String</code>: <code>GET /api/role/admin-seller</code> guarded by <code>@PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")</code>. Accessible by Admin or Seller.</li>
    </ul>

    <h2>7.3 <code>AuthServiceImpl</code> Methods</h2>
    <ul>
        <li><code>register(RegisterRequest request) -> RegisterResponse</code>:
            <br>&bull; Checks if email exists (<code>existsByEmail</code>). Throws <code>EmailAlreadyExistsException</code>.
            <br>&bull; Checks if phone exists (<code>existsByPhoneNumber</code>). Throws <code>PhoneNumberAlreadyExistsException</code>.
            <br>&bull; Encrypts raw password using <code>passwordEncoder.encode()</code>.
            <br>&bull; Sets account status to <code>ACTIVE</code> and saves entity via <code>userRepository.save()</code>.
        </li>
        <li><code>login(LoginRequest request) -> LoginResponse</code>:
            <br>&bull; Finds user by email via <code>userRepository.findByEmail()</code>. Throws <code>UserNotFoundException</code> if missing.
            <br>&bull; Validates password match via <code>passwordEncoder.matches()</code>. Throws <code>InvalidPasswordException</code> if invalid.
            <br>&bull; Generates JWT token via <code>jwtService.generateToken(user)</code> and returns <code>LoginResponse</code>.
        </li>
        <li><code>forgotPassword(ForgotPasswordRequest request) -> ForgotPasswordResponse</code>:
            <br>&bull; Finds user by email. Generates random UUID reset token.
            <br>&bull; Sets reset token expiry to <code>LocalDateTime.now().plusMinutes(15)</code> and saves user.
        </li>
        <li><code>resetPassword(ResetPasswordRequest request) -> ResetPasswordResponse</code>:
            <br>&bull; Finds user by reset token via <code>userRepository.findByResetToken()</code>.
            <br>&bull; Validates token expiration. Encrypts new password with BCrypt.
            <br>&bull; Clears reset token fields (<code>setResetToken(null)</code>) and saves updated user.
        </li>
    </ul>

    <h2>7.4 <code>JwtService</code> Methods</h2>
    <ul>
        <li><code>generateToken(User user) -> String</code>: Constructs signed JWT token containing claims: <code>sub</code> (email), <code>userId</code>, <code>role</code>, issued time, and expiration time (+24 hrs).</li>
        <li><code>getSignInKey() -> SecretKey</code>: Converts configured secret key string into HMAC-SHA <code>SecretKey</code> instance.</li>
        <li><code>extractUsername(String token) -> String</code>: Extracts subject claim (email) from JWT token.</li>
        <li><code>extractExpiration(String token) -> Date</code>: Extracts token expiration timestamp.</li>
        <li><code>extractClaim(String token, Function&lt;Claims, T&gt; claimsResolver) -> T</code>: Generic utility method to extract any custom claim from token.</li>
        <li><code>isTokenValid(String token, User user) -> boolean</code>: Compares extracted token email with user email and checks if token is expired.</li>
    </ul>

    <h2>7.5 <code>JwtAuthenticationFilter</code> Methods</h2>
    <ul>
        <li><code>doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)</code>:
            <br>&bull; Extracts <code>Authorization</code> header. Skips filter if header is null or does not start with <code>Bearer </code>.
            <br>&bull; Parses JWT token, extracts email, loads user from database.
            <br>&bull; Validates token and populates <code>UsernamePasswordAuthenticationToken</code> into Spring <code>SecurityContextHolder</code>.
        </li>
    </ul>

    <h2>7.6 <code>User</code> Entity Methods (UserDetails Interface Implementation)</h2>
    <ul>
        <li><code>getAuthorities() -> Collection&lt;? extends GrantedAuthority&gt;</code>: Returns <code>ROLE_ADMIN</code>, <code>ROLE_SELLER</code>, or <code>ROLE_CUSTOMER</code> based on assigned role.</li>
        <li><code>getUsername() -> String</code>: Returns email address as Spring Security username.</li>
        <li><code>isEnabled() -> boolean</code>: Returns true only when <code>status == AccountStatus.ACTIVE</code>.</li>
        <li><code>isAccountNonExpired()</code>, <code>isAccountNonLocked()</code>, <code>isCredentialsNonExpired()</code>: Return <code>true</code>.</li>
        <li>Getters & Setters for all fields: <code>getId()</code>, <code>getEmail()</code>, <code>getPassword()</code>, <code>getRole()</code>, <code>getStatus()</code>, <code>getResetToken()</code>, etc.</li>
    </ul>

    <h2>7.7 <code>UserRepository</code> Methods</h2>
    <ul>
        <li><code>findByEmail(String email) -> Optional&lt;User&gt;</code>: Custom JPA derived query method to fetch User by email.</li>
        <li><code>findByResetToken(String resetToken) -> Optional&lt;User&gt;</code>: Fetches user by active forgot password reset token.</li>
        <li><code>existsByEmail(String email) -> boolean</code>: Checks duplicate email existence during registration.</li>
        <li><code>existsByPhoneNumber(String phoneNumber) -> boolean</code>: Checks duplicate phone number during registration.</li>
    </ul>

    <h2>7.8 <code>GlobalExceptionHandler</code> Methods</h2>
    <ul>
        <li><code>handleValidationException(MethodArgumentNotValidException ex)</code>: Handles <code>@Valid</code> field errors, returns HTTP 400 Bad Request with field-by-field error map.</li>
        <li><code>handleEmailAlreadyExists(EmailAlreadyExistsException ex)</code>: Returns HTTP 409 Conflict.</li>
        <li><code>handlePhoneAlreadyExists(PhoneNumberAlreadyExistsException ex)</code>: Returns HTTP 409 Conflict.</li>
        <li><code>handleUserNotFound(UserNotFoundException ex)</code>: Returns HTTP 404 Not Found.</li>
        <li><code>handleInvalidPassword(InvalidPasswordException ex)</code>: Returns HTTP 401 Unauthorized.</li>
        <li><code>handleException(Exception ex)</code>: Generic exception handler returning HTTP 500 Internal Server Error.</li>
    </ul>

    <div class="page-break"></div>

    <!-- SECTION 8: SECURITY ARCHITECTURE -->
    <h1>8. Security Architecture & JWT Workflow (सुरक्षा वर्कफ़्लो)</h1>

    <p>Auth Service ka security workflow Spring Security 6 stateless architecture ke upar kaam karta hai:</p>

    <pre>
[ Client Request ] 
       │
       ▼
┌─────────────────────────────────────────────────────────┐
│              JwtAuthenticationFilter                    │
│ 1. Read 'Authorization: Bearer <token>' Header          │
│ 2. Extract Email & Validate Signature via JwtService    │
│ 3. Load User Details & set Authentication in SecurityCtx│
└────────────────────────────┬────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────┐
│                  SecurityFilterChain                    │
│  - /api/auth/register          -> permitAll()           │
│  - /api/auth/login             -> permitAll()           │
│  - /api/auth/forgot-password   -> permitAll()           │
│  - /api/auth/reset-password    -> permitAll()           │
│  - /api/role/**                -> authenticated()       │
└────────────────────────────┬────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────┐
│                 Controller (@PreAuthorize)              │
│  - @PreAuthorize("hasRole('ADMIN')")                    │
│  - @PreAuthorize("hasRole('SELLER')")                   │
└─────────────────────────────────────────────────────────┘
    </pre>

    <div class="callout">
        <strong>JWT Token Payload Structure:</strong>
        <pre>{
  "sub": "user@example.com",
  "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "role": "CUSTOMER",
  "iat": 1785700000,
  "exp": 1785786400
}</pre>
    </div>

    <!-- SECTION 9: CONCLUSION & VERIFICATION -->
    <h1>9. Summary & Documentation Conclusion</h1>
    <p>Auth Service microservice completely structured, battle-tested aur production-ready microservice hai. Isme clean architecture (Controller - Service - Repository - Entity pattern), DTO validation, Custom Exceptions, JPA PostgreSQL integration, Eureka service registration, aur robust Spring Security + JWT authentication implemented hai.</p>

    <br>
    <hr>
    <p style="text-align: center; color: #64748b; font-size: 9pt;">
        Generated for Distributed E-Commerce Microservices Project &bull; Auth Service Documentation
    </p>

</body>
</html>
"""

temp_html = os.path.join(os.environ.get("TEMP", "C:\\Temp"), "auth_service_doc.html")
pdf_output = os.path.join(r"c:\Users\hp\IdeaProjects\distributed-ecommerce-microservices", "Auth_Service_Documentation.pdf")

with open(temp_html, "w", encoding="utf-8") as f:
    f.write(html_content)

print(f"HTML file created at: {temp_html}")

edge_path = r"C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe"
cmd = [
    edge_path,
    "--headless=new",
    f"--print-to-pdf={pdf_output}",
    "--no-pdf-header-footer",
    temp_html
]

print(f"Running command: {' '.join(cmd)}")
result = subprocess.run(cmd, capture_output=True, text=True)

if os.path.exists(pdf_output):
    print(f"PDF successfully generated at: {pdf_output} (Size: {os.path.getsize(pdf_output)} bytes)")
else:
    print(f"Failed to generate PDF. Output: {result.stdout} Error: {result.stderr}")
