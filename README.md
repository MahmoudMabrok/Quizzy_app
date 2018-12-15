# Quizzy
Quizzy is an android application that aims to provide the teacher with a tool to evaluate his students.

## Table of Contents
* [Synopsis](#Synopsis)
* [Recognition of Need](#Recognition-of-Need)
* [Related work](#Related-work)
* [Our Solution](#Our-Solution)
* [Inspiration](#Inspiration)
* [Technologies](#Technologies)
* [Features](#Features)
* [Screenshots](#Screenshots)

## Synopsis
Software engineering is not only coding. In fact it is understanding society problems, gathering requirements, Designing solutions and lastly coding shows up as a single stage in the entire process. And to be able to find a problem and innovate a solution for it you must have a deep understanding of the problem itself and the community in which the problem appears as well. Based on that, we have decided to solve one of the problems in our educational system in Egypt in particular and in most developing countries in general because it is the system that we understand most as we are involved in it and suffer from its problems.

## Recognition of Need
One of the most important activities in the process of education is evaluation. Any professor, teacher or instructor needs to evaluate his students many times throughout the entire course period. Traditionally in developing countries like Egypt in order for this activity to be carried out, the instructor has to quiz his students many times. In each time the instructor has to print quizzes on papers, receive his students’ answers, evaluate them manually, and record their grades manually as well. This process may occur once a week and recall that in developing countries the number of students in a class may exceed a hundred or more. This process takes from 6 to 10 hours weekly for a given course and the instructor most frequently is in charge of more than one course. This process may take much time than what the process of teaching itself takes so it is a waste of time, effort and money.

## Related work
There are many tools that are dedicated to provide a powerful and collaborative learning environment which are known as LMSs (Learning Management Systems) . Here, we investigate only one of these systems which is **Moodle**.

### Moodle
#### Features
* Provides a full-fledged, collaborative learning environment.
* Provides a smart android app for student.
* Can import quizzes or entire courses from Blackboard or WebCT.
* Student can download course contents, quizzes and access them offline but with less functionality of online access.

#### Limitations
* Administration is overwhelming and not user-friendly.
* Technical skills are needed to use the system.

## Our Solution
Quizzy provides a simple solution for the evaluation problem through a mobile application. Quizzy has been built with simplicity as one of the main requirements to fit developing countries. For the teacher, he/she simply renders the quiz and provides correct answers of the questions and let Quizzy evaluates students' answers automatically for him. For the student, he/she opens the quiz, solve the questions, submit, and let Quizzy tells him his grade on this quiz immediately after submitting. 

## Inspiration
There have been two things inspired us to build Quizzy:
1. to compete in "IEEEmadc 2018" competition. "IEEEmadc" is a world-wide mobile application development competition organized by IEEE.
1. the egyptian government recently started to improve the educational process by providing each student a ‘Tablet’ device. That event has provided a fertilized environment for a digital solution like Quizzy.

## Technologies
Quizzy is built using:

Technology | Version
---------- | -------
Java | 8
Kotlin | 1.2.50 
XML | 1.0
Firebase Services | 11.8.0
Facebook Login | [4,5)
Play Services Auth | 11.8.0
RxJava | 1.3.0
RxAndroid | 1.2.1
RxFirebase | 1.5.0
Dagger | 2.4
Butter Knife | 8.8.1
Crash Reporter | 1.0.9
Event Bus | 3.1.1
MP Android Chart | 3.0.3
Plain Pie | 0.2.9
LeonidsLib | 1.3.2
Fancy Walkthrough | 2.1
SpinKit | 1.2.0
Circular Progress Bar | 1.0.1 

## Features
Quizzy provides several features including:
* User(Teacher/Student) can register/login using one of three methods:
  * Email/Password
  * Facebook account
  * Google account
* Teacher can render quizzes.
* Teacher can publish any quiz any time he wishes. Once a quiz is published, students can access and submit it.
* Teacher can specify a duration for a quiz. Once a student opens the quiz, a timer starts and once the time is over the student can not submit his/her answers.
* Till now, only multiple-choice questions are supported.
* Teacher can see reports on:
  * Individual quizzes.
  * Individual students.
* Student can access and submit any quiz once it is published by his/her teacher.
* Student can see his grade immediately after submitting the quiz.
* Student can see report on his performance in all quizzes.
* App rewards (and publish) students with high marks in each quiz as a motivation for students to do their best

## Screenshots
* Walkthrough<br />
![Walkthrough GIF](https://github.com/MahmoudMabrok/Quizzy_app/blob/master/20181215_163439.gif)
* Login/Register
* Teacher Home
* Student Home
* Creating Quiz by Teacher
* Accessing Quiz by Student
* Reports for Teacher
* Reports for Student
* Rewards
