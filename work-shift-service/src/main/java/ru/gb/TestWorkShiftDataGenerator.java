//package ru.gb;
//
//import com.github.javafaker.Faker;
//import jakarta.annotation.PostConstruct;
//import org.springframework.stereotype.Component;
//import ru.gb.api.WorkShift;
//import ru.gb.repository.WorkShiftRepository;
//
//import java.time.ZoneId;
//import java.util.*;
//
//@Component
//public class TestWorkShiftDataGenerator {
//
//    private final WorkShiftRepository workShiftRepository;
//
//
//    public TestWorkShiftDataGenerator(WorkShiftRepository workShiftRepository) {
//        this.workShiftRepository = workShiftRepository;
//    }
//
//    /**
//     * Генерация списка сотрудников для проверки
//     */
//    @PostConstruct
//    void generateData() {
//        Faker faker = new Faker();
//        Random rnd = new Random();
//        List<WorkShift> workShiftList = new ArrayList<>();
//        for (long i = 0; i < 15; i++) {
//            Date between = faker.date().between(startOfMonth(), endOfMonth());
//            long randomEmployeeId = rnd.nextLong(1, 6);
//            workShiftList.add(new WorkShift(
//                    randomEmployeeId,
//                    between.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
//                    faker.random().nextInt(1, 8),
//                    "none"));
//        }
//        workShiftRepository.saveAll(workShiftList);
//    }
//
//    private Date startOfMonth() {
//        Calendar instance = Calendar.getInstance();
//        instance.set(Calendar.YEAR, 2024);
//        instance.set(Calendar.MONTH, Calendar.MARCH);
//        instance.set(Calendar.DAY_OF_MONTH, 1);
//        return  instance.getTime();
//    }
//
//    private Date endOfMonth() {
//        Calendar instance = Calendar.getInstance();
//        instance.set(Calendar.YEAR, 2024);
//        instance.set(Calendar.MONTH, Calendar.MARCH);
//        instance.set(Calendar.DAY_OF_MONTH, 31);
//        return  instance.getTime();
//    }
//
//}
