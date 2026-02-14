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


