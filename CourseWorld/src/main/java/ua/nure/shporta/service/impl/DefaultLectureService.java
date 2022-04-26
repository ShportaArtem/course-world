package ua.nure.shporta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ua.nure.shporta.dao.CourseDAO;
import ua.nure.shporta.dao.LectureDAO;
import ua.nure.shporta.model.Course;
import ua.nure.shporta.model.Lecture;
import ua.nure.shporta.service.LectureService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DefaultLectureService implements LectureService {
    @Value("${page.lectures.size}")
    private Integer pageSize;

    @Autowired
    private LectureDAO lectureDAO;
    @Autowired
    private CourseDAO courseDAO;

    @Override
    public Page<Lecture> findLecturesByCourseId(Optional<Integer> page, Integer courseId) {
        Course course = courseDAO.getById(courseId);
        int currentPage = page.orElse(1);

        return lectureDAO.findAllByCourseOrderByPositionAsc(course, PageRequest.of(currentPage - 1, pageSize));
    }

    @Override
    public Lecture createLecture(Lecture lecture) {
        Course course = lecture.getCourse();
        lecture = lectureDAO.save(lecture);
        List<Lecture> lectureList = course.getLectures();
        Integer position = lecture.getPosition();
        if (lectureList != null && position != lectureList.size() + 1) {
            lectureList = lectureList.stream().filter(l -> l.getPosition() >= position)
                    .peek(l -> l.setPosition(l.getPosition() + 1)).collect(Collectors.toList());
            lectureDAO.saveAll(lectureList);
        }
        lectureDAO.flush();
        return lecture;
    }

    @Override
    public Lecture findLectureById(Integer id) {
        return lectureDAO.getById(id);
    }

    @Override
    public Lecture findLectureByCourseAndPosition(Course course, Integer position) {
        return lectureDAO.findLectureByCourseAndPosition(course, position).get();
    }

    @Override
    public boolean hasPreviousLecture(Lecture lecture) {
        List<Lecture> lectureList = lecture.getCourse().getLectures();
        return lectureList.stream().anyMatch(l -> l.getPosition() == lecture.getPosition() - 1);
    }

    @Override
    public boolean hasNextLecture(Lecture lecture) {
        List<Lecture> lectureList = lecture.getCourse().getLectures();
        return lectureList.stream().anyMatch(l -> l.getPosition() == lecture.getPosition() + 1);
    }

    @Override
    public Lecture findNextLecture(Lecture lecture) {
        return lectureDAO.findLectureByCourseAndPosition(lecture.getCourse(), lecture.getPosition() + 1).orElse(null);
    }

    @Override
    public Lecture findPreviousLecture(Lecture lecture) {
        return lectureDAO.findLectureByCourseAndPosition(lecture.getCourse(), lecture.getPosition() - 1).orElse(null);
    }
}
