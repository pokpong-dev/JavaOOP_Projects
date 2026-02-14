# อ่านตรงนี้เด้อ


---

## แบ่งงานใหม่



### คน 1 : Database
**หน้าที่:** รับผิดชอบเรื่องการเก็บข้อมูลลง Database (MySQL/SQLite) เพื่อให้ปิดโปรแกรมแล้วเปิดใหม่ข้อมูลยังอยู่
*   [ ] สร้าง Class ใหม่ `DatabaseHandler.java` (หรือเขียนใน `TaskManager`)
*   [ ] เขียน method `connectDB()` เพื่อเชื่อมต่อฐานข้อมูล
*   [ ] เขียน SQL: `CREATE TABLE tasks (...)`
*   [ ] แก้ไข `TaskManager.java`:
    *   `addTask`: เมื่อกดเพิ่ม -> ให้ `INSERT INTO tasks ...`
    *   `removeTask`: เมื่อกดลบ -> ให้ `DELETE FROM tasks ...`
    *   `Constructor`: เมื่อเปิดโปรแกรม -> ให้ `SELECT * FROM tasks` แล้วโหลดลง List

### คน 2 : Save PDF
**หน้าที่:** รับผิดชอบเรื่องการออกรายงานเป็น PDF 
*   [ ] หาวิธี save pdf
*   [ ] แก้ `SingleTask.java` และ `Task` ตัวอื่นๆ -> เขียน method `getDetailsForReport()` ให้ return ข้อมูลสวยๆ
*   [ ] แก้ `TaskManager.exportAllToFile()`:
    *   เปลี่ยนจากเขียน Text File ธรรมดา เป็นสร้างไฟล์ PDF

### คน 3: ผู้สร้าง RepeatingTask 
**หน้าที่:** สร้าง Class ใหม่ตาม Diagram และจัดการเรื่อง Task ที่ต้องทำซ้ำ
*   [ ] สร้างไฟล์ `src/com/pomodoro/model/RepeatingTask.java`
*   [ ] เขียนให้ extends `Task`
*   [ ] เพิ่ม field `intervalDays`, `nextOccurrence`
*   [ ] เขียน Logic คำนวณวันถัดไป
*   [ ] ประสานงานกับคนทำ Database: ถ้ามันมีพวก colum อะ 

### คนที่ 4: ผู้สร้าง DeadlineTask 
**หน้าที่:** สร้าง Class ใหม่และดูเรื่องเวลาพวกการแจ้งเตือน
*   [ ] สร้างไฟล์ `src/com/pomodoro/model/DeadlineTask.java`
*   [ ] เขียนให้ extends `Task` และเพิ่ม field `isUrgent`
*   [ ] เขียน Logic เช็คว่าถ้าใกล้ถึงกำหนด (Due Date) ให้แจ้งเตือน หรือเปลี่ยนสี
*   [ ] ไปดูไฟล์ `PomodoroTimer.java`  ดูว่าเวลา `TotalFocusMinutes` ถูกบันทึกลง Task ถูกต้องไหม
*   [ ] ประสานงานกับคนทำ Database

---

## โครงสร้าง folder



### 1. ไฟล์ที่ต้อง "สร้างเพิ่มใหม่" (Create New Files) ไม่จำเป็นต้องทำตาม
ใน `src/com/pomodoro/model/` 

*   **`RepeatingTask.java`**:
    *   ต้อง `extends Task`
    *   ต้องมี method `exportToText()` (หรือเตรียมข้อมูลสำหรับ PDF)
*   **`DeadlineTask.java`**:
    *   ต้อง `extends Task`
    *   ต้องมี method `checkUrgency()`

---

### 2. ไฟล์ที่ต้อง "แก้ไขไส้ใน" (Edit Existing Files)
ตอนนี้สร้างโครงไว้ให้แล้ว แต่มันเป็นแค่ dummy โง่ๆ ไม่ใช่ของจริง เราต้องใส่ code logic จริงๆเข้าไป 

#### `com.pomodoro.logic.TaskManager.java`


*   **`addTask / removeTask`**: ตอนนี้ใช้ `ArrayList` ธรรมดา **ซึ่งมันต้องเซฟเป็น txt หรือเป็น database บลาๆที่อยากใช้อะ**
*   **`exportAllToFile(String filepath)`**:
    *   ตอนนี้มันแค่ปริ้น `System.out.println` เฉยๆ
    *   **หน้าที่เรา:** ต้องเปลี่ยนไปใช้PDF สร้างไฟล์ report
*   **`sortByName / sortByPriority`**:
    *   **ทางเลือก 1:** ดึงข้อมูลมาทั้งหมดแล้วใช้ `Collections.sort()` ใน Java
    *   **ทางเลือก 2:** ใช้ SQL

#### `com.pomodoro.model.SingleTask.java`
*   **`exportToText()`**:
    *   ปรับปรุงให้ return ข้อมูลสำหรับเอาไปใส่ใน PDF 

#### `com.pomodoro.model.Category.java`
*   ตอนนี้มีแค่ field พื้นฐาน (`id`, `name`, `colorCode`)

*   ถ้า Database มีตาราง `categories` แยกต่างหากก็ต้องมาเพิ่ม Logic ตรงนี้ด้วย

---

### 1. **Branch หลัก **
*   `main`: **ห้ามใครแก้ตรงๆ** ต้อง Merge เข้ามาเท่านั้น

### 2. **Branch ของแต่ละคน (Feature Branches)**
ให้ทุกคนแตก Branch จาก `main` ไปทำงานของตัวเอง:

*   **คนที่ 1 (Database)**: สร้าง branch ชื่อ `feature/database`
    *   ทำงานเกี่ยวกับ SQL
*   **คนที่ 2 (PDF)**: สร้าง branch ชื่อ `feature/pdf-export`
    *   ทำงานเกี่ยวกับ PDF
*   **คนที่ 3 (Repeating)**: สร้าง branch ชื่อ `feature/repeating-task`
    *   สร้างไฟล์ RepeatingTask.java
*   **คนที่ 4 (Deadline)**: สร้าง branch ชื่อ `feature/deadline-task`
    *   สร้างไฟล์ DeadlineTask.java, Timer Logic บลาๆ

### 3. **วิธีทำงาน**
1.  **Clone** โปรเจกต์ลงเครื่อง ใช้ git หรือกดโหลดเอาก็ได้
2.  **Checkout** ไปที่ Branch ตัวเอง: `git checkout -b feature/ชื่อฟีเจอร์`
3.  **เขียนโค้ด** จนเสร็จ
4.  **Commit & Push** ขึ้น GitHub
5.  **Pull Request (PR)** กลับมาที่ `main` ช่วยกันหา error


