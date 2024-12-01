package org.korsnaike.migrations

import org.korsnaike.db.Migration

class `0212024_0241_add_new_test_data_to_student_table`: Migration() {
    override fun up() {
        db.executeUpdate(
            "INSERT INTO student (first_name, last_name, middle_name, email, telegram, phone, git)\n" +
                    "VALUES\n" +
                    "    ('Максим', 'Федоров', 'Викторович', 'maxim.fedorov@example.com', '@maximfedorov', '+79011223344', 'https://github.com/maximfedorov'),\n" +
                    "    ('Виктория', 'Зайцева', 'Петровна', 'viktoria.zaytseva@example.com', '@viktoriazaytseva', '+79665544332', 'https://github.com/viktoriazaytseva'),\n" +
                    "    ('Никита', 'Лебедев', 'Сергеевич', 'nikita.lebedev@example.com', '@nikitalevedev', '+79122334455', 'https://github.com/nikitalevedev'),\n" +
                    "    ('Светлана', 'Павлова', 'Михайловна', 'svetlana.pavlova@example.com', '@svetlanapavlova', '+79887766554', 'https://github.com/svetlanapavlova'),\n" +
                    "    ('Григорий', 'Смирнов', 'Николаевич', 'grigory.smirnov@example.com', '@grigorysmirnov', '+79786543210', 'https://github.com/grigorysmirnov'),\n" +
                    "    ('Анастасия', 'Тарасова', 'Алексеевна', 'anastasiya.tarasova@example.com', '@anastasiytarasova', '+79054321678', 'https://github.com/anastasiytarasova'),\n" +
                    "    ('Роман', 'Ковалев', 'Анатольевич', 'roman.kovalev@example.com', '@romankovalev', '+79123456700', 'https://github.com/romankovalev'),\n" +
                    "    ('Татьяна', 'Николаева', 'Игоревна', 'tatiana.nikolaeva@example.com', '@tatiananikolaeva', '+79223456789', 'https://github.com/tatiananikolaeva'),\n" +
                    "    ('Евгений', 'Алексеев', 'Станиславович', 'evgeniy.alekseev@example.com', '@evgeniyalekseev', '+79345678123', 'https://github.com/evgeniyalekseev'),\n" +
                    "    ('Алиса', 'Григорьева', 'Андреевна', 'alisa.grigoreva@example.com', '@alisagrigoreva', '+79098765432', 'https://github.com/alisagrigoreva'),\n" +
                    "    ('Денис', 'Сафонов', 'Дмитриевич', 'denis.safonov@example.com', '@denissafonov', '+79123429876', 'https://github.com/denissafonov'),\n" +
                    "    ('Людмила', 'Королева', 'Сергеевна', 'lyudmila.koroleva@example.com', '@lyudmilakoroleva', '+79561234567', 'https://github.com/lyudmilakoroleva'),\n" +
                    "    ('Валерий', 'Фомин', 'Павлович', 'valeriy.fomin@example.com', '@valeriyfomin', '+79631234567', 'https://github.com/valeriyfomin'),\n" +
                    "    ('Маргарита', 'Коваленко', 'Геннадиевна', 'margarita.kovalenko@example.com', '@margaritakovalenko', '+79012349876', 'https://github.com/margaritakovalenko'),\n" +
                    "    ('Станислав', 'Данилов', 'Валерьевич', 'stanislav.danilov@example.com', '@stanislavdanilov', '+79123456780', 'https://github.com/stanislavdanilov'),\n" +
                    "    ('Олег', 'Морозов', 'Викторович', 'oleg.morozov@example.com', '@olegmorozov', '+79657894312', 'https://github.com/olegmorozov'),\n" +
                    "    ('Наталья', 'Ковальчук', 'Станиславовна', 'natalya.kovalchuk@example.com', '@natalyakovalchuk', '+79065432134', 'https://github.com/natalyakovalchuk'),\n" +
                    "    ('Тимур', 'Щербаков', 'Русланович', 'timur.scherbakov@example.com', '@timurscherbakov', '+79183334455', 'https://github.com/timurscherbakov'),\n" +
                    "    ('Элина', 'Панина', 'Ильинична', 'elina.panina@example.com', '@elinapanina', '+79050987654', 'https://github.com/elinapanina');"
        )
    }

    override fun down() {
        db.executeUpdate("DELETE FROM student")
    }
}