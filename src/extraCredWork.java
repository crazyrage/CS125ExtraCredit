import java.util.*;
import java.lang.*;
import java.io.*;

public class extraCredWork {
    
    public static void main(String[] args){
    	
        Scanner scanner = new Scanner(System.in);
        SchedulerList schedule = new SchedulerList();
        
        while(true){
            System.out.println("Welcome to the Course Scheduler! Select an option below:");
            System.out.println("1) add an course");
            System.out.println("2) remove a course");
            System.out.println("3) display courses alphabetically");
            System.out.println("4) display courses in major");
            System.out.println("5) display schedule for day");
            System.out.println("6) display total number of credit hours");
            System.out.println("7) quit");
            System.out.println("Select an option above:");
            						
            int input = Integer.parseInt(scanner.nextLine());
            
            if(input == 1){
            	System.out.println("Enter course info e.g. \"CS 125 Introduction-to-Computer-Science 3 MWF 12 true\"");
                String addInfo = scanner.nextLine();
                System.out.println(schedule.addCourse(addInfo));
                
            }
            if(input == 2){
            	System.out.println("Select a course to remove e.g. \"CS 125\"");
                System.out.println(schedule.display());
                String[] removeCourse = scanner.nextLine().split(" ");
                System.out.println(schedule.removeCourse(removeCourse[0], Integer.parseInt(removeCourse[1])));
                
            }
            if(input == 3){
            	System.out.println(schedule.display());
                
            }
            if(input == 4){
            	System.out.println(schedule.displayMajor());
                
            }
            if(input == 5){
            	System.out.println("Select a day to view schedule MTWRF");
                char day = scanner.nextLine().charAt(0);
                System.out.println(schedule.displaySchedule(day));
                
            }
            if(input == 6){
            	System.out.println("Total credit hours: " + schedule.totalCreditHours() + "\n");
                
            }
            if(input == 7){
            	System.out.println("Are you sure you want to quit? Data will be lost.");
                boolean quit = scanner.nextBoolean();
                if(quit){
                    return;
                }
                break;
            
            }
        }
    }
}

class ListEntry{

    Course course;
    ListEntry next;

    public ListEntry(Course course){
        this.course = course;
    }

    public ListEntry(ListEntry other){
        this.course = other.course;
    }
}
class Course{
    
    String dept, desc, days;
    int course, start, credits;
    boolean req;
    
    public Course(String info){
        String[] split = info.split(" ");
        this.dept = split[0];
        this.course = Integer.parseInt(split[1]);
        this.desc = split[2];
        this.credits = Integer.parseInt(split[3]);
        this.days = split[4];
        this.start = Integer.parseInt(split[5]);
        this.req = Boolean.parseBoolean(split[6]);
    }

    public String info(){
        String req = this.req ? "*" : "";
        return dept + " " + Integer.toString(course) + " " + desc + " " + Integer.toString(credits) + " " + days + " " + Integer.toString(start) + " " + req;
    }

    public boolean conflicts(Course other){
        if(other.start == this.start){
            for(char day : this.days.toCharArray()){
                if(other.days.indexOf(day) >= 0) return true;
            }
        }
        return false;
    }
}

class SchedulerList{    
    ListEntry head;

    public String addCourse(String courseInfo){
        Course course = new Course(courseInfo);
        if(head == null){
            head = new ListEntry(course);
        } else { 
            ListEntry curr = head;
            while(curr != null){
                if(curr.course.conflicts(course)){
                    return "Conflict with " + curr.course.dept + " " + Integer.toString(curr.course.course) + " " + curr.course.desc + "\n";
                }
                curr = curr.next;
            }
            curr = head;
            while(curr != null){
                if(curr == head && course.desc.compareTo(curr.course.desc) < 0){
                    ListEntry entry = new ListEntry(course);
                    entry.next = curr;
                    head = entry;
                    break;
                }

                if(curr.next == null && course.desc.compareTo(curr.course.desc) > 0){
                    curr.next = new ListEntry(course);
                    break;
                }
                if(course.desc.compareTo(curr.course.desc) > 0 && course.desc.compareTo(curr.next.course.desc) < 0){
                    ListEntry entry = new ListEntry(course);
                    entry.next = curr.next;
                    curr.next = entry;
                    break;
                }
                curr = curr.next;
            }
        }
        return "Added " + course.info() + "\n";
    }

    public String removeCourse(String dept, int course){
        ListEntry curr = head;
        if(curr.course.course == course && curr.course.dept.equals(dept)){
         head = curr.next;
         return "Removed course " + dept + " " + Integer.toString(course) + "\n";
        }
        while(curr.next != null){
            if(curr.next.course.course == course && curr.next.course.dept.equals(dept)){
                curr.next = curr.next.next;
                return "Removed course " + dept + " " + Integer.toString(course) + "\n";
            }
            curr = curr.next;
        }

        return "Course not found\n";
    }

    public String display(){
        String display = "";
        ListEntry curr = head;
        do {
            display += curr.course.info() + "\n";
            curr = curr.next;
        } while(curr != null);
        return display;
    }

    public String displayMajor(){
        String display = "";
        ListEntry curr = head;
        do {
            if(curr.course.req == true){
                display += curr.course.info() + "\n";
            }
            curr = curr.next;
        } while(curr != null);
        return display;
    }

    public String displaySchedule(char day){
        ListEntry cur = head;
        ListEntry scheduleHead = null;
        while(cur != null){
            if(cur.course.days.indexOf(day) >= 0){
                if(scheduleHead == null){
                	scheduleHead = new ListEntry(cur);
                } else {
                    ListEntry scheduleCur = scheduleHead;
                    while(scheduleCur != null){
                        if(scheduleCur == scheduleHead && cur.course.start < scheduleHead.course.start){
                            ListEntry entry = new ListEntry(cur);
                            entry.next = scheduleHead;
                            scheduleHead = entry;
                            break;
                        }
                        if(scheduleCur.next == null && cur.course.start > scheduleCur.course.start){
                            ListEntry entry = new ListEntry(cur);
                            scheduleCur.next = entry;
                            break;
                        }
                        if(cur.course.start > scheduleCur.course.start && cur.course.start < scheduleCur.next.course.start){
                            ListEntry entry = new ListEntry(cur);
                            entry.next = scheduleCur.next;
                            scheduleCur.next = entry;
                            break;
                        }
                        scheduleCur = scheduleCur.next;
                    }
                }
            }
            cur = cur.next;
        }
        ListEntry sched = scheduleHead;
        String today = "";
        do {
            today += scheduleHead.course.info() + "\n";
            scheduleHead = scheduleHead.next;
        } while(scheduleHead != null);
        return today;
    }

    public int totalCreditHours(){
        int tHrs = 0;
        ListEntry curr = head;
        do {
        	tHrs += curr.course.credits;
            curr = curr.next;
        } while(curr != null);
        return tHrs;
    }
}


