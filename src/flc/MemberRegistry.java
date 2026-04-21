package flc;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class MemberRegistry {

    private final Map<String, Member> members = new LinkedHashMap<>();

    public MemberRegistry() {
        addMember(new Member("M1", "Alice Johnson"));
        addMember(new Member("M2", "Bob Smith"));
        addMember(new Member("M3", "Carol White"));
        addMember(new Member("M4", "David Brown"));
        addMember(new Member("M5", "Eva Green"));
        addMember(new Member("M6", "Frank Lee"));
        addMember(new Member("M7", "Grace Hall"));
        addMember(new Member("M8", "Henry Clark"));
        addMember(new Member("M9", "Ivy Walker"));
        addMember(new Member("M10", "Jack Turner"));
        addMember(new Member("M11", "Kathy Adams"));
    }

    public void addMember(Member m) {
        members.put(m.getMemberId(), m);
    }
    public Member findById(String id){
        return members.get(id);
    }
    public Collection<Member> getAllMembers(){
        return members.values();
    }
    public boolean memberIdExists(String id) {
        return members.containsKey(id);
    }

    public void printMembers() {
        System.out.println("\n-------------------------");
        System.out.println("| ID    | Name             |");
        System.out.println("-------------------------");
        for (Member m : members.values()) {
            System.out.printf("| %-5s | %-16s |%n", m.getMemberId(), m.getName());
        }

    }
}