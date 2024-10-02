package org.korsnaike.migrations

import org.korsnaike.db.Migration

class `03102024_0004_add_test_data_to_student_table` : Migration() {
    override fun up() {
        db.executeUpdate(
            "INSERT INTO student (first_name, last_name, middle_name, email, telegram, phone, git)\n" +
                "VALUES\n" +
                "    ('Иван', 'Петров', 'Сергеевич', 'ivan.petrov@example.com', '@ivanpetrov', '+79001234567', 'https://github.com/ivanpetrov'),\n" +
                "    ('Мария', 'Иванова', 'Алексеевна', 'maria.ivanova@example.com', '@mariaivanova', '+79087654321', 'https://github.com/mariaivanova'),\n" +
                "    ('Александр', 'Сидоров', 'Дмитриевич', 'aleksandr.sidorov@example.com', '@aleksandrsidorov', '+79112233445', 'https://github.com/aleksandrsidorov'),\n" +
                "    ('Елена', 'Кузнецова', 'Михайловна', 'elena.kuznetsova@example.com', '@elenakuznetsova', '+79556667788', 'https://github.com/elenakuznetsova'),\n" +
                "    ('Дмитрий', 'Романов', 'Владимирович', 'dmitriy.romanov@example.com', '@dmitriyromanov', '+79998887766', 'https://github.com/dmitriyromanov'),\n" +
                "    ('Анна', 'Смирнова', 'Сергеевна', 'anna.smirnova@example.com', '@annasmirnova', '+79123456789', 'https://github.com/annasmirnova'),\n" +
                "    ('Сергей', 'Соколов', 'Игоревич', 'sergey.sokolov@example.com', '@sergeysokolov', '+79987654321', 'https://github.com/sergeysokolov'),\n" +
                "    ('Екатерина', 'Орлова', 'Андреевна', 'ekaterina.orlova@example.com', '@ekaterinaorlova', '+79112223344', 'https://github.com/ekaterinaorlova'),\n" +
                "    ('Артем', 'Павлов', 'Максимович', 'artem.pavlov@example.com', '@artempavlov', '+79556677888', 'https://github.com/artempavlov'),\n" +
                "    ('Ольга', 'Михайлова', 'Викторовна', 'olga.mihaylova@example.com', '@olgamihaylova', '+79995887766', 'https://github.com/olgamihaylova');"
        )
    }

    override fun down() {
        db.executeUpdate("DELETE FROM student")
    }

}