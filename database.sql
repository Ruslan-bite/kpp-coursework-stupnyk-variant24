DROP DATABASE IF EXISTS museum_exhibits;
CREATE DATABASE museum_exhibits;
USE museum_exhibits;

-- Таблиця експонатів
CREATE TABLE exhibit (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    inventory_code VARCHAR(40) NOT NULL,
    name VARCHAR(160) NOT NULL,
    material VARCHAR(80) NOT NULL,
    hall VARCHAR(120) NOT NULL,
    material_sensitivity DOUBLE NOT NULL,
    exhibition_significance DOUBLE NOT NULL
);

-- Таблиця фахівців
CREATE TABLE specialist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(160) NOT NULL,
    position VARCHAR(120) NOT NULL,
    specialization VARCHAR(120) NOT NULL
);

-- Таблиця оглядів
CREATE TABLE inspection (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exhibit_id BIGINT NOT NULL,
    inspection_date DATE NOT NULL,
    damage_severity DOUBLE NOT NULL,
    notes VARCHAR(255),
    FOREIGN KEY (exhibit_id) REFERENCES exhibit(id)
);

-- Таблиця заявок на консервацію
CREATE TABLE conservation_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    inspection_id BIGINT NOT NULL,
    specialist_id BIGINT NOT NULL,
    state VARCHAR(20) NOT NULL,
    f1 DOUBLE NOT NULL,
    f2 DOUBLE NOT NULL,
    f3 DOUBLE NOT NULL,
    risk_index DOUBLE NOT NULL,
    description VARCHAR(255),
    FOREIGN KEY (inspection_id) REFERENCES inspection(id),
    FOREIGN KEY (specialist_id) REFERENCES specialist(id)
);

INSERT INTO exhibit (inventory_code, name, material, hall, material_sensitivity, exhibition_significance) VALUES
('EXH-001', 'Козацька шабля XVIII ст.', 'Метал', 'Зала козацької доби', 45.0, 70.0),
('EXH-002', 'Рукописне Євангеліє', 'Пергамент', 'Зала стародруків', 85.0, 90.0),
('EXH-003', 'Гобелен «Полтавська битва»', 'Текстиль', 'Зала історії', 90.0, 80.0);

INSERT INTO specialist (full_name, position, specialization) VALUES
('Коваленко Ірина Петрівна', 'Реставратор', 'Папір і пергамент'),
('Мельник Олег Васильович', 'Консерватор', 'Метал і текстиль');

INSERT INTO inspection (exhibit_id, inspection_date, damage_severity, notes) VALUES
(2, '2026-10-05', 65.0, 'Осипання фарбового шару на полях'),
(3, '2026-10-08', 30.0, 'Локальне вигоряння тканини'),
(1, '2026-10-10', 10.0, 'Незначна поверхнева корозія');