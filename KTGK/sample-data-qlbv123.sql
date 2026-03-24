-- Script dữ liệu mẫu tiếng Việt cho CSDL qlbv123
-- Lưu ý: Bảng sẽ được Spring Boot tự tạo (ddl-auto=update)
-- Chạy app 1 lần để tạo bảng, sau đó copy script này vào MySQL để insert dữ liệu.

USE qlbv123;

-- 1) Tạo role nếu chưa có
INSERT INTO role(name)
SELECT 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'ADMIN');

INSERT INTO role(name)
SELECT 'PATIENT'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'PATIENT');

-- 2) Tạo khoa
INSERT INTO department(name)
SELECT 'Khoa Nội tổng quát' WHERE NOT EXISTS (SELECT 1 FROM department WHERE name = 'Khoa Nội tổng quát');
INSERT INTO department(name)
SELECT 'Khoa Tim mạch' WHERE NOT EXISTS (SELECT 1 FROM department WHERE name = 'Khoa Tim mạch');
INSERT INTO department(name)
SELECT 'Khoa Nhi' WHERE NOT EXISTS (SELECT 1 FROM department WHERE name = 'Khoa Nhi');
INSERT INTO department(name)
SELECT 'Khoa Da liễu' WHERE NOT EXISTS (SELECT 1 FROM department WHERE name = 'Khoa Da liễu');
INSERT INTO department(name)
SELECT 'Khoa Răng Hàm Mặt' WHERE NOT EXISTS (SELECT 1 FROM department WHERE name = 'Khoa Răng Hàm Mặt');

-- 3) Tạo bác sĩ mẫu (10 bác sĩ để test phân trang)
INSERT INTO doctor(name, image, specialty, department_id)
SELECT 'BS. Nguyễn Văn An', 'https://i.pravatar.cc/400?img=11', 'Nội khoa', d.id
FROM department d
WHERE d.name = 'Khoa Nội tổng quát'
  AND NOT EXISTS (SELECT 1 FROM doctor WHERE name = 'BS. Nguyễn Văn An');

INSERT INTO doctor(name, image, specialty, department_id)
SELECT 'BS. Trần Thị Bích', 'https://i.pravatar.cc/400?img=12', 'Tim mạch can thiệp', d.id
FROM department d
WHERE d.name = 'Khoa Tim mạch'
  AND NOT EXISTS (SELECT 1 FROM doctor WHERE name = 'BS. Trần Thị Bích');

INSERT INTO doctor(name, image, specialty, department_id)
SELECT 'BS. Lê Minh Khoa', 'https://i.pravatar.cc/400?img=13', 'Nhi tổng quát', d.id
FROM department d
WHERE d.name = 'Khoa Nhi'
  AND NOT EXISTS (SELECT 1 FROM doctor WHERE name = 'BS. Lê Minh Khoa');

INSERT INTO doctor(name, image, specialty, department_id)
SELECT 'BS. Phạm Thu Hà', 'https://i.pravatar.cc/400?img=14', 'Da liễu thẩm mỹ', d.id
FROM department d
WHERE d.name = 'Khoa Da liễu'
  AND NOT EXISTS (SELECT 1 FROM doctor WHERE name = 'BS. Phạm Thu Hà');

INSERT INTO doctor(name, image, specialty, department_id)
SELECT 'BS. Võ Quốc Bảo', 'https://i.pravatar.cc/400?img=15', 'Răng hàm mặt', d.id
FROM department d
WHERE d.name = 'Khoa Răng Hàm Mặt'
  AND NOT EXISTS (SELECT 1 FROM doctor WHERE name = 'BS. Võ Quốc Bảo');

INSERT INTO doctor(name, image, specialty, department_id)
SELECT 'BS. Đoàn Thành Sơn', 'https://i.pravatar.cc/400?img=16', 'Nội tiết', d.id
FROM department d
WHERE d.name = 'Khoa Nội tổng quát'
  AND NOT EXISTS (SELECT 1 FROM doctor WHERE name = 'BS. Đoàn Thành Sơn');

INSERT INTO doctor(name, image, specialty, department_id)
SELECT 'BS. Hoàng Mai Linh', 'https://i.pravatar.cc/400?img=17', 'Suy tim', d.id
FROM department d
WHERE d.name = 'Khoa Tim mạch'
  AND NOT EXISTS (SELECT 1 FROM doctor WHERE name = 'BS. Hoàng Mai Linh');

INSERT INTO doctor(name, image, specialty, department_id)
SELECT 'BS. Nguyễn Ngọc Châu', 'https://i.pravatar.cc/400?img=18', 'Nhi hô hấp', d.id
FROM department d
WHERE d.name = 'Khoa Nhi'
  AND NOT EXISTS (SELECT 1 FROM doctor WHERE name = 'BS. Nguyễn Ngọc Châu');

INSERT INTO doctor(name, image, specialty, department_id)
SELECT 'BS. Trần Gia Hân', 'https://i.pravatar.cc/400?img=19', 'Điều trị mụn', d.id
FROM department d
WHERE d.name = 'Khoa Da liễu'
  AND NOT EXISTS (SELECT 1 FROM doctor WHERE name = 'BS. Trần Gia Hân');

INSERT INTO doctor(name, image, specialty, department_id)
SELECT 'BS. Đặng Quang Huy', 'https://i.pravatar.cc/400?img=20', 'Nha chu', d.id
FROM department d
WHERE d.name = 'Khoa Răng Hàm Mặt'
  AND NOT EXISTS (SELECT 1 FROM doctor WHERE name = 'BS. Đặng Quang Huy');

-- 4) Tạo tài khoản admin mẫu
-- Password gốc: admin123
INSERT INTO patient(username, password, email)
SELECT 'admin',
       '$2a$10$7fVwENx6ztF8QhM4f67vA.5Z2J2M4SjwzQqB2kWfWfX2b4f6Ahvva',
       'admin@qlbv.vn'
WHERE NOT EXISTS (SELECT 1 FROM patient WHERE username = 'admin');

-- Gán quyền ADMIN cho tài khoản admin
INSERT INTO patient_role(patient_id, role_id)
SELECT p.id, r.role_id
FROM patient p
JOIN role r ON r.name = 'ADMIN'
WHERE p.username = 'admin'
  AND NOT EXISTS (
    SELECT 1
    FROM patient_role pr
    WHERE pr.patient_id = p.id
      AND pr.role_id = r.role_id
);
