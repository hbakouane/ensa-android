package com.valet.app.data.db;

import com.valet.app.data.entity.Category;
import com.valet.app.data.entity.Club;
import com.valet.app.data.entity.Event;
import com.valet.app.data.entity.User;
import com.valet.app.util.DateUtil;
import com.valet.app.util.PasswordUtil;

import java.util.concurrent.Executors;

public class DatabaseSeeder {

    public static void seedIfNeeded(AppDatabase db) {
        Executors.newSingleThreadExecutor().execute(() -> {
            if (db.categoryDao().count() > 0) return;

            // Seed categories
            String[][] cats = {
                    {"Technology", "ic_technology"},
                    {"Sports", "ic_sports"},
                    {"Arts", "ic_arts"},
                    {"Music", "ic_music"},
                    {"Science", "ic_science"},
                    {"Literature", "ic_literature"},
                    {"Gaming", "ic_gaming"},
                    {"Volunteering", "ic_volunteering"}
            };
            long[] catIds = new long[cats.length];
            for (int i = 0; i < cats.length; i++) {
                Category c = new Category();
                c.name = cats[i][0];
                c.iconName = cats[i][1];
                catIds[i] = db.categoryDao().insert(c);
            }

            // Seed clubs
            long now = System.currentTimeMillis();
            Object[][] clubs = {
                    {"ENSA Tech Club", "Explore cutting-edge technology, coding workshops, and hackathons.", catIds[0]},
                    {"Robotics Lab", "Build and program robots for competitions and research projects.", catIds[0]},
                    {"Football Club", "Weekly matches, tournaments, and fitness training sessions.", catIds[1]},
                    {"Basketball Squad", "Practice sessions and inter-university basketball competitions.", catIds[1]},
                    {"Art & Design Studio", "Creative workshops in painting, digital art, and graphic design.", catIds[2]},
                    {"ENSA Music Band", "Jam sessions, concerts, and music production workshops.", catIds[3]},
                    {"Physics Society", "Experiments, guest lectures, and science fair preparations.", catIds[4]},
                    {"Book Club", "Monthly reading challenges, book discussions, and author meetups.", catIds[5]},
                    {"Esports Team", "Competitive gaming, LAN parties, and streaming events.", catIds[6]},
                    {"Volunteer Corps", "Community service, charity drives, and social impact projects.", catIds[7]}
            };

            long[] clubIds = new long[clubs.length];
            for (int i = 0; i < clubs.length; i++) {
                Club club = new Club();
                club.name = (String) clubs[i][0];
                club.description = (String) clubs[i][1];
                club.categoryId = (long) clubs[i][2];
                club.createdAt = now;
                clubIds[i] = db.clubDao().insert(club);
            }

            // Seed events (dates relative to now - always upcoming)
            Object[][] events = {
                    {"Intro to AI Workshop", "Learn the basics of artificial intelligence and machine learning.", clubIds[0], "Amphi A", 2, 14, 0, 120},
                    {"Hackathon 2025", "24-hour coding challenge with amazing prizes!", clubIds[0], "Lab 3", 5, 9, 0, 1440},
                    {"Web Dev Bootcamp", "Full-stack web development crash course.", clubIds[0], "Salle Info 2", 8, 10, 0, 180},
                    {"Robot Wars", "Inter-club robotics competition.", clubIds[1], "Hall B", 3, 15, 0, 240},
                    {"Arduino Workshop", "Hands-on Arduino programming session.", clubIds[1], "Lab 1", 10, 14, 0, 120},
                    {"Football Tournament", "Annual inter-department football championship.", clubIds[2], "Stadium", 1, 16, 0, 180},
                    {"Fitness Workshop", "Learn proper workout techniques and nutrition.", clubIds[2], "Gym", 7, 10, 0, 90},
                    {"Basketball 3v3", "3-on-3 basketball mini tournament.", clubIds[3], "Court", 4, 17, 0, 120},
                    {"Art Exhibition", "Student art showcase and gallery opening.", clubIds[4], "Gallery Hall", 6, 18, 0, 180},
                    {"Watercolor Workshop", "Learn watercolor painting techniques.", clubIds[4], "Art Room", 12, 14, 0, 120},
                    {"Open Mic Night", "Perform your favorite songs or original pieces.", clubIds[5], "Auditorium", 3, 20, 0, 150},
                    {"Music Production 101", "Learn beat-making with FL Studio.", clubIds[5], "Music Room", 9, 15, 0, 120},
                    {"Physics Quiz Night", "Test your physics knowledge in teams.", clubIds[6], "Amphi B", 2, 18, 0, 90},
                    {"Science Fair Prep", "Prepare your projects for the annual science fair.", clubIds[6], "Lab 2", 11, 10, 0, 180},
                    {"Book Discussion: Dune", "Monthly discussion on Frank Herbert's Dune.", clubIds[7], "Library", 1, 17, 0, 90},
                    {"Creative Writing Workshop", "Improve your storytelling and writing skills.", clubIds[7], "Salle 5", 8, 14, 0, 120},
                    {"League of Legends Tournament", "5v5 competitive LoL tournament.", clubIds[8], "Gaming Room", 4, 14, 0, 300},
                    {"Game Dev Workshop", "Create your first game with Unity.", clubIds[8], "Lab 3", 13, 10, 0, 180},
                    {"Beach Cleanup", "Community beach cleanup and environmental awareness.", clubIds[9], "City Beach", 6, 8, 0, 240},
                    {"Charity Bake Sale", "Bake and sell treats to fund local charities.", clubIds[9], "Campus Entrance", 3, 10, 0, 360}
            };

            for (Object[] e : events) {
                Event event = new Event();
                event.title = (String) e[0];
                event.description = (String) e[1];
                event.clubId = (long) e[2];
                event.location = (String) e[3];
                event.dateTime = DateUtil.daysFromNowAtHour((int) e[4], (int) e[5], (int) e[6]);
                event.durationMinutes = (int) e[7];
                event.createdAt = now;
                db.eventDao().insert(event);
            }

            // Seed test user
            User testUser = new User();
            testUser.email = "test@valet.com";
            testUser.passwordHash = PasswordUtil.hash("password123");
            testUser.name = "Test User";
            testUser.bio = "ENSA student exploring campus life.";
            testUser.createdAt = now;
            db.userDao().insert(testUser);
        });
    }
}
