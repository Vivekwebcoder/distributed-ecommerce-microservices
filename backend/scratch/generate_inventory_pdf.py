import os
import subprocess
import sys

html_content = """<!DOCTYPE html>
<html lang="hi">
<head>
    <meta charset="UTF-8">
    <title>Inventory Service - API & Technical Documentation</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&family=Fira+Code:wght@400;500&display=swap');

        @page {
            size: A4;
            margin: 15mm 12mm 15mm 12mm;
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
            color: #0f172a;
            background-color: #ffffff;
            line-height: 1.5;
            font-size: 10pt;
            margin: 0;
            padding: 0;
        }

        /* Cover Page */
        .cover-page {
            height: 96vh;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            background: linear-gradient(135deg, #064e3b 0%, #047857 50%, #059669 100%);
            color: #ffffff;
            padding: 45px 35px;
            border-radius: 12px;
            page-break-after: always;
        }

        .cover-header {
            border-bottom: 2px solid rgba(255, 255, 255, 0.25);
            padding-bottom: 20px;
        }

        .cover-badge {
            display: inline-block;
            background: rgba(255, 255, 255, 0.2);
            border: 1px solid #6ee7b7;
            color: #a7f3d0;
            padding: 6px 16px;
            border-radius: 20px;
            font-size: 9.5pt;
            font-weight: 700;
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
            font-weight: 500;
            color: #d1fae5;
            margin: 0;
        }

        .cover-body {
            margin: 30px 0;
        }

        .cover-desc {
            font-size: 11pt;
            color: #ecfdf5;
            line-height: 1.7;
        }

        .cover-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 15px;
            margin-top: 25px;
        }

        .cover-card {
            background: rgba(255, 255, 255, 0.12);
            border: 1px solid rgba(255, 255, 255, 0.2);
            padding: 16px;
            border-radius: 8px;
        }

        .cover-card-label {
            font-size: 8.5pt;
            text-transform: uppercase;
            color: #a7f3d0;
            font-weight: 700;
            letter-spacing: 0.5px;
        }

        .cover-card-value {
            font-size: 12.5pt;
            font-weight: 700;
            color: #ffffff;
            margin-top: 4px;
        }

        .cover-footer {
            border-top: 1px solid rgba(255, 255, 255, 0.25);
            padding-top: 15px;
            display: flex;
            justify-content: space-between;
            font-size: 9pt;
            color: #a7f3d0;
        }

        /* Headings */
        h1 {
            font-size: 18pt;
            font-weight: 800;
            color: #064e3b;
            border-bottom: 3px solid #059669;
            padding-bottom: 6px;
            margin-top: 25px;
            margin-bottom: 15px;
            page-break-after: avoid;
        }

        h2 {
            font-size: 13pt;
            font-weight: 700;
            color: #047857;
            margin-top: 20px;
            margin-bottom: 10px;
            border-left: 4px solid #10b981;
            padding-left: 8px;
            page-break-after: avoid;
        }

        h3 {
            font-size: 11pt;
            font-weight: 700;
            color: #1e293b;
            margin-top: 14px;
            margin-bottom: 6px;
            page-break-after: avoid;
        }

        p {
            margin-bottom: 10px;
            text-align: justify;
        }

        ul, ol {
            margin-top: 4px;
            margin-bottom: 12px;
            padding-left: 20px;
        }

        li {
            margin-bottom: 4px;
        }

        /* Tables */
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 12px 0 18px 0;
            font-size: 9pt;
        }

        tr {
            page-break-inside: avoid;
        }

        th {
            background-color: #064e3b;
            color: #ffffff;
            font-weight: 700;
            text-align: left;
            padding: 8px 10px;
            border: 1px solid #064e3b;
        }

        td {
            padding: 8px 10px;
            border: 1px solid #cbd5e1;
            vertical-align: top;
        }

        tr:nth-child(even) td {
            background-color: #f0fdf4;
        }

        /* Badges */
        .badge {
            display: inline-block;
            padding: 3px 8px;
            border-radius: 4px;
            font-size: 8pt;
            font-weight: 800;
            font-family: 'Fira Code', monospace;
            text-transform: uppercase;
        }

        .badge-post { background-color: #dcfce7; color: #15803d; border: 1px solid #86efac; }
        .badge-get { background-color: #dbeafe; color: #1d4ed8; border: 1px solid #93c5fd; }
        .badge-put { background-color: #fef3c7; color: #b45309; border: 1px solid #fde68a; }
        .badge-delete { background-color: #fee2e2; color: #b91c1c; border: 1px solid #fca5a5; }

        /* Code Blocks */
        code, pre {
            font-family: 'Fira Code', Consolas, monospace;
            font-size: 8.5pt;
        }

        code {
            background-color: #f1f5f9;
            color: #047857;
            padding: 2px 5px;
            border-radius: 4px;
            border: 1px solid #e2e8f0;
        }

        pre {
            background-color: #0f172a;
            color: #f8fafc;
            padding: 12px;
            border-radius: 8px;
            overflow-x: auto;
            margin: 10px 0;
            line-height: 1.4;
            white-space: pre-wrap;
            word-break: break-all;
        }

        /* Stat Grid */
        .stat-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 10px;
            margin: 15px 0;
        }

        .stat-box {
            background: #f0fdf4;
            border: 1px solid #bbf7d0;
            border-top: 3px solid #059669;
            padding: 12px;
            border-radius: 6px;
            text-align: center;
        }

        .stat-number {
            font-size: 18pt;
            font-weight: 800;
            color: #059669;
            line-height: 1;
        }

        .stat-label {
            font-size: 8pt;
            color: #475569;
            font-weight: 700;
            margin-top: 5px;
            text-transform: uppercase;
        }

        /* Callout Box */
        .callout {
            background-color: #ecfdf5;
            border-left: 4px solid #059669;
            padding: 12px 16px;
            border-radius: 0 8px 8px 0;
            margin: 15px 0;
            font-size: 9.5pt;
        }

        .callout-title {
            font-weight: 700;
            color: #064e3b;
            margin-bottom: 4px;
        }

        .api-card {
            background: #ffffff;
            border: 1px solid #cbd5e1;
            border-radius: 8px;
            padding: 16px;
            margin: 18px 0;
            box-shadow: 0 1px 3px rgba(0,0,0,0.05);
            page-break-inside: avoid;
        }

        .api-card-header {
            display: flex;
            align-items: center;
            gap: 10px;
            border-bottom: 1px solid #e2e8f0;
            padding-bottom: 10px;
            margin-bottom: 12px;
        }

        .api-url {
            font-family: 'Fira Code', monospace;
            font-weight: 700;
            font-size: 10pt;
            color: #0f172a;
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
            <span class="cover-badge">Microservices Technical Guide</span>
            <h1 class="cover-title">Inventory Service<br>API Testing & Architecture Manual</h1>
            <p class="cover-subtitle">Distributed E-Commerce Microservices System</p>
        </div>

        <div class="cover-body">
            <p class="cover-desc">
                Yeh document <strong>Inventory Service</strong> ki sabhi REST APIs ka complete testing manual hai. Isme har API ka URL, HTTP Method, Purpose (kyu banaya gaya hai), Database Table & Columns used, Request Payload, aur Successful Response Payload ki poori jaankari di gayi hai.
            </p>

            <div class="cover-grid">
                <div class="cover-card">
                    <div class="cover-card-label">Service Port & Base Path</div>
                    <div class="cover-card-value">HTTP / 8084 (`/api/v1/inventory`)</div>
                </div>
                <div class="cover-card">
                    <div class="cover-card-label">Database & Table</div>
                    <div class="cover-card-value">PostgreSQL (`ecommerce_db` -> `inventory`)</div>
                </div>
                <div class="cover-card">
                    <div class="cover-card-label">Concurrency Control</div>
                    <div class="cover-card-value">Optimistic Locking (`@Version`)</div>
                </div>
                <div class="cover-card">
                    <div class="cover-card-label">Java & Spring Boot</div>
                    <div class="cover-card-value">Java 21 | Spring Boot 3.2.5</div>
                </div>
            </div>
        </div>

        <div class="cover-footer">
            <div>Project: Distributed E-Commerce Microservices</div>
            <div>Author: Vivek Web Coder</div>
            <div>Date: August 2026</div>
        </div>
    </div>

    <!-- EXECUTIVE OVERVIEW -->
    <h1>1. Executive Summary & Inventory Overview</h1>
    <p>Inventory Service pure E-Commerce Ecosystem ka <strong>Real-Time Stock Management Engine</strong> hai. Yeh service Product SKUs ka stock level, reserve stock, reorder thresholds, batch availability checks, aur direct stock additions/reductions ko handle karti hai.</p>

    <div class="stat-grid">
        <div class="stat-box">
            <div class="stat-number">6</div>
            <div class="stat-label">REST APIs</div>
        </div>
        <div class="stat-box">
            <div class="stat-number">1</div>
            <div class="stat-label">DB Table (`inventory`)</div>
        </div>
        <div class="stat-box">
            <div class="stat-number">3</div>
            <div class="stat-label">Stock Status States</div>
        </div>
        <div class="stat-box">
            <div class="stat-number">8084</div>
            <div class="stat-label">Server Port</div>
        </div>
    </div>

    <div class="callout">
        <div class="callout-title">💡 Why Inventory Service is Critical?</div>
        Online shopping system mein jab koi customer checkout karta hai, to system ko milliseconds mein verify karna hota hai ki requested SKU stock mein hai ya nahi. Multi-item cart validation, stock over-selling protection, aur automated low-stock warnings ke liye Inventory Service banaya gaya hai.
    </div>

    <!-- DATABASE SCHEMA SECTION -->
    <h1>2. Database Table Structure & Details (डेटाबेस तालिका विवरण)</h1>
    <p>Inventory Service <strong>`ecommerce_db`</strong> database ke andar <strong>`inventory`</strong> table ka upyog karti hai.</p>

    <h2>2.1 Table Name: <code>inventory</code></h2>
    <table>
        <thead>
            <tr>
                <th>Column Name</th>
                <th>SQL Data Type</th>
                <th>Constraints</th>
                <th>Description (विवरण)</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><code>id</code></td>
                <td>BIGINT</td>
                <td>PRIMARY KEY, AUTO IDENTITY</td>
                <td>Unique record ID for inventory item</td>
            </tr>
            <tr>
                <td><code>sku_code</code></td>
                <td>VARCHAR(100)</td>
                <td>NOT NULL, UNIQUE, INDEX (`idx_inventory_sku`)</td>
                <td>Unique Product SKU Code (e.g. <code>IPHONE-15-PRO-256GB</code>)</td>
            </tr>
            <tr>
                <td><code>quantity</code></td>
                <td>INTEGER</td>
                <td>NOT NULL</td>
                <td>Total physical stock available in warehouse</td>
            </tr>
            <tr>
                <td><code>reserved_quantity</code></td>
                <td>INTEGER</td>
                <td>NOT NULL, DEFAULT 0</td>
                <td>Stock reserved for orders pending payment/shipment</td>
            </tr>
            <tr>
                <td><code>reorder_threshold</code></td>
                <td>INTEGER</td>
                <td>NOT NULL, DEFAULT 10</td>
                <td>Threshold limit below which status becomes <code>LOW_STOCK</code></td>
            </tr>
            <tr>
                <td><code>status</code></td>
                <td>VARCHAR(30)</td>
                <td>NOT NULL</td>
                <td>Enum Status: <code>IN_STOCK</code>, <code>LOW_STOCK</code>, <code>OUT_OF_STOCK</code></td>
            </tr>
            <tr>
                <td><code>version</code></td>
                <td>BIGINT</td>
                <td>NOT NULL, DEFAULT 0</td>
                <td>Optimistic locking version field for concurrent updates</td>
            </tr>
            <tr>
                <td><code>created_at</code></td>
                <td>TIMESTAMP</td>
                <td>NOT NULL, READONLY</td>
                <td>Timestamp when inventory entry was created</td>
            </tr>
            <tr>
                <td><code>updated_at</code></td>
                <td>TIMESTAMP</td>
                <td>NOT NULL</td>
                <td>Timestamp when stock was last updated</td>
            </tr>
        </tbody>
    </table>

    <h2>2.2 StockStatus Enum States</h2>
    <ul>
        <li><strong><code>IN_STOCK</code></strong>: Quantity &gt; Reorder Threshold (Stock aamtor par available hai).</li>
        <li><strong><code>LOW_STOCK</code></strong>: 0 &lt; Quantity &le; Reorder Threshold (Stock kam ho gaya hai, restock ki zaroorat hai).</li>
        <li><strong><code>OUT_OF_STOCK</code></strong>: Quantity &le; 0 (Stock bilkul khatam ho gaya hai).</li>
    </ul>

    <div class="page-break"></div>

    <!-- API DETAILS SECTION -->
    <h1>3. Complete Inventory REST APIs Testing Guide</h1>
    <p>Niche sabhi 6 REST APIs ka URL, Purpose, Table, Request, aur Response ka pura detail diya gaya hai:</p>

    <!-- API 1 -->
    <div class="api-card">
        <div class="api-card-header">
            <span class="badge badge-post">POST</span>
            <span class="api-url">/api/v1/inventory</span>
        </div>
        <p><strong>1. API Name:</strong> Create Inventory Entry (Naye SKU Ka Stock Initialize Karna)</p>
        <p><strong>2. Kyu Banaya Gaya Hai (Purpose):** Jab seller ya admin naya product list karta hai, to uss product SKU ka initial stock record create karne ke liye.</p>
        <p><strong>3. Table Used:</strong> <code>inventory</code> (Inserts 1 row)</p>
        
        <h3>Request Body (JSON):</h3>
<pre>{
  "skuCode": "IPHONE-15-PRO-256GB",
  "quantity": 100,
  "reorderThreshold": 10
}</pre>

        <h3>Response Payload (HTTP 201 CREATED):</h3>
<pre>{
  "success": true,
  "message": "Inventory created successfully",
  "data": {
    "id": 1,
    "skuCode": "IPHONE-15-PRO-256GB",
    "quantity": 100,
    "reservedQuantity": 0,
    "reorderThreshold": 10,
    "status": "IN_STOCK",
    "createdAt": "2026-08-05T00:15:00",
    "updatedAt": "2026-08-05T00:15:00"
  },
  "timestamp": "2026-08-05T00:15:00"
}</pre>
    </div>

    <!-- API 2 -->
    <div class="api-card">
        <div class="api-card-header">
            <span class="badge badge-get">GET</span>
            <span class="api-url">/api/v1/inventory/{skuCode}</span>
        </div>
        <p><strong>1. API Name:</strong> Get Inventory Details by SKU Code</p>
        <p><strong>2. Kyu Banaya Gaya Hai (Purpose):** Product detail page ya dashboard par kisi specific SKU Code ka total stock, reserved stock, aur current status check karne ke liye.</p>
        <p><strong>3. Table Used:</strong> <code>inventory</code> (Reads record by <code>sku_code</code>)</p>
        <p><strong>4. Example Request URL:</strong> <code>http://localhost:8084/api/v1/inventory/IPHONE-15-PRO-256GB</code></p>

        <h3>Response Payload (HTTP 200 OK):</h3>
<pre>{
  "success": true,
  "message": "Inventory fetched successfully",
  "data": {
    "id": 1,
    "skuCode": "IPHONE-15-PRO-256GB",
    "quantity": 100,
    "reservedQuantity": 0,
    "reorderThreshold": 10,
    "status": "IN_STOCK",
    "createdAt": "2026-08-05T00:15:00",
    "updatedAt": "2026-08-05T00:15:00"
  },
  "timestamp": "2026-08-05T00:16:00"
}</pre>
    </div>

    <div class="page-break"></div>

    <!-- API 3 -->
    <div class="api-card">
        <div class="api-card-header">
            <span class="badge badge-put">PUT</span>
            <span class="api-url">/api/v1/inventory/{skuCode}/stock</span>
        </div>
        <p><strong>1. API Name:</strong> Admin Restock / Overwrite Stock Level</p>
        <p><strong>2. Kyu Banaya Gaya Hai (Purpose):** Warehouse Manager ya Admin dwara warehouse mein naye shipment aane par total physical stock quantity ko overwrite/update karne ke liye.</p>
        <p><strong>3. Table Used:</strong> <code>inventory</code> (Updates <code>quantity</code>, <code>updated_at</code>, <code>status</code>)</p>
        <p><strong>4. Example Request URL:</strong> <code>http://localhost:8084/api/v1/inventory/IPHONE-15-PRO-256GB/stock</code></p>

        <h3>Request Body (JSON):</h3>
<pre>{
  "skuCode": "IPHONE-15-PRO-256GB",
  "quantity": 250,
  "referenceId": "RESTOCK-BATCH-2026-001"
}</pre>

        <h3>Response Payload (HTTP 200 OK):</h3>
<pre>{
  "success": true,
  "message": "Stock updated successfully",
  "data": {
    "id": 1,
    "skuCode": "IPHONE-15-PRO-256GB",
    "quantity": 250,
    "reservedQuantity": 0,
    "reorderThreshold": 10,
    "status": "IN_STOCK",
    "createdAt": "2026-08-05T00:15:00",
    "updatedAt": "2026-08-05T00:17:00"
  },
  "timestamp": "2026-08-05T00:17:00"
}</pre>
    </div>

    <!-- API 4 -->
    <div class="api-card">
        <div class="api-card-header">
            <span class="badge badge-post">POST</span>
            <span class="api-url">/api/v1/inventory/reduce</span>
        </div>
        <p><strong>1. API Name:</strong> Reduce Physical Stock (Stock Deduction)</p>
        <p><strong>2. Kyu Banaya Gaya Hai (Purpose):** Order confirm hone par ya stock damage/adjustment par existing stock mein se specified quantity minus (reduce) karne ke liye.</p>
        <p><strong>3. Table Used:</strong> <code>inventory</code> (Deducts <code>quantity</code> = <code>quantity - delta</code>)</p>

        <h3>Request Body (JSON):</h3>
<pre>{
  "skuCode": "IPHONE-15-PRO-256GB",
  "quantity": 5,
  "referenceId": "ORDER-99481"
}</pre>

        <h3>Response Payload (HTTP 200 OK):</h3>
<pre>{
  "success": true,
  "message": "Stock reduced successfully",
  "data": {
    "id": 1,
    "skuCode": "IPHONE-15-PRO-256GB",
    "quantity": 245,
    "reservedQuantity": 0,
    "reorderThreshold": 10,
    "status": "IN_STOCK",
    "createdAt": "2026-08-05T00:15:00",
    "updatedAt": "2026-08-05T00:18:00"
  },
  "timestamp": "2026-08-05T00:18:00"
}</pre>
    </div>

    <div class="page-break"></div>

    <!-- API 5 -->
    <div class="api-card">
        <div class="api-card-header">
            <span class="badge badge-post">POST</span>
            <span class="api-url">/api/v1/inventory/increase</span>
        </div>
        <p><strong>1. API Name:</strong> Increase Physical Stock (Stock Addition / Return)</p>
        <p><strong>2. Kyu Banaya Gaya Hai (Purpose):** Customer dwara order cancel karne par ya item warehouse mein return aane par stock dubara add (increase) karne ke liye.</p>
        <p><strong>3. Table Used:</strong> <code>inventory</code> (Increases <code>quantity</code> = <code>quantity + delta</code>)</p>

        <h3>Request Body (JSON):</h3>
<pre>{
  "skuCode": "IPHONE-15-PRO-256GB",
  "quantity": 2,
  "referenceId": "RETURN-CANCEL-1204"
}</pre>

        <h3>Response Payload (HTTP 200 OK):</h3>
<pre>{
  "success": true,
  "message": "Stock increased successfully",
  "data": {
    "id": 1,
    "skuCode": "IPHONE-15-PRO-256GB",
    "quantity": 247,
    "reservedQuantity": 0,
    "reorderThreshold": 10,
    "status": "IN_STOCK",
    "createdAt": "2026-08-05T00:15:00",
    "updatedAt": "2026-08-05T00:19:00"
  },
  "timestamp": "2026-08-05T00:19:00"
}</pre>
    </div>

    <!-- API 6 -->
    <div class="api-card">
        <div class="api-card-header">
            <span class="badge badge-post">POST</span>
            <span class="api-url">/api/v1/inventory/check</span>
        </div>
        <p><strong>1. API Name:</strong> Batch Stock Check (Multi-Item Cart Availability)</p>
        <p><strong>2. Kyu Banaya Gaya Hai (Purpose):** Shopping Cart / Order Service dwara ek hi request mein multiple products ka stock check karne ke liye (e.g. Cart mein 2 iPhone aur 1 MacBook hai to dono ka stock ek sath verify karna).</p>
        <p><strong>3. Table Used:</strong> <code>inventory</code> (Reads multiple SKU records)</p>

        <h3>Request Body (JSON List):</h3>
<pre>[
  {
    "skuCode": "IPHONE-15-PRO-256GB",
    "quantity": 2
  },
  {
    "skuCode": "MACBOOK-PRO-M3-512GB",
    "quantity": 1
  }
]</pre>

        <h3>Response Payload (HTTP 200 OK):</h3>
<pre>{
  "success": true,
  "message": "Stock availability check completed",
  "data": [
    {
      "skuCode": "IPHONE-15-PRO-256GB",
      "requestedQuantity": 2,
      "availableQuantity": 247,
      "isAvailable": true
    },
    {
      "skuCode": "MACBOOK-PRO-M3-512GB",
      "requestedQuantity": 1,
      "availableQuantity": 0,
      "isAvailable": false
    }
  ],
  "timestamp": "2026-08-05T00:20:00"
}</pre>
    </div>

    <!-- SUMMARY SECTION -->
    <h1>4. Testing Verification & Summary</h1>
    <p>Aap upar diye gaye JSON Payloads ko <strong>Postman</strong>, <strong>cURL</strong>, ya <strong>Swagger UI (`http://localhost:8084/swagger-ui.html`)</strong> ka upyog karke aasani se test kar sakte hain.</p>

    <br><hr>
    <p style="text-align: center; color: #64748b; font-size: 8.5pt;">
        Inventory Service API Testing Documentation &bull; Distributed E-Commerce Microservices &bull; August 2026
    </p>

</body>
</html>
"""

html_output_path = r"c:\Users\hp\IdeaProjects\distributed-ecommerce-microservices\Inventory_Service_Documentation.html"
pdf_output_path = r"c:\Users\hp\IdeaProjects\distributed-ecommerce-microservices\Inventory_Service_Documentation.pdf"

with open(html_output_path, "w", encoding="utf-8") as f:
    f.write(html_content)

print(f"HTML file created at: {html_output_path}")

edge_path = r"C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe"
cmd = [
    edge_path,
    "--headless=new",
    f"--print-to-pdf={pdf_output_path}",
    "--no-pdf-header-footer",
    html_output_path
]

print(f"Running Edge command to convert HTML to PDF: {' '.join(cmd)}")
result = subprocess.run(cmd, capture_output=True, text=True)

if os.path.exists(pdf_output_path):
    print(f"PDF successfully generated at: {pdf_output_path} (Size: {os.path.getsize(pdf_output_path)} bytes)")
else:
    print(f"Failed to generate PDF. Output: {result.stdout} Error: {result.stderr}")
