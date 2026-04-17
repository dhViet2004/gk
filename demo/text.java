// 1. Chọn Design Pattern phù hợp và Giải thích (0.5 điểm)
// Lựa chọn: Strategy Pattern (Mẫu chiến lược).

// Lý do lựa chọn:

// Đa thuật toán: Bài toán yêu cầu hệ thống xử lý nhiều cách gợi ý khác nhau (Sở thích, Lịch sử, Xu hướng) cho cùng một mục đích. Strategy cho phép đóng gói các thuật toán này thành các lớp riêng biệt.

// Nguyên lý Open/Closed: Khi cần thêm cách gợi ý mới trong tương lai, ta chỉ cần tạo thêm một lớp "Chiến lược" mới thực thi interface mà không cần sửa đổi mã nguồn hiện tại của lớp xử lý chính.

// Linh hoạt tại Runtime: Cho phép hệ thống hoặc người dùng thay đổi "chiến thuật" gợi ý ngay khi chương trình đang chạy tùy thuộc vào ngữ cảnh hoặc dữ liệu sinh viên.

// @startuml
// interface IRecommendationStrategy {
//     + recommend(studentID: String): List<Event>
// }

// class InterestBasedStrategy implements IRecommendationStrategy {
//     + recommend(studentID: String): List<Event>
// }

// class HistoryBasedStrategy implements IRecommendationStrategy {
//     + recommend(studentID: String): List<Event>
// }

// class TrendingStrategy implements IRecommendationStrategy {
//     + recommend(studentID: String): List<Event>
// }

// class RecommendationContext {
//     - strategy: IRecommendationStrategy
//     + setStrategy(strategy: IRecommendationStrategy): void
//     + getRecommendations(studentID: String): List<Event>
// }

// RecommendationContext o-- IRecommendationStrategy : uses
// @enduml


import java.util.*;

// 1. Interface định nghĩa chiến lược chung
interface IRecommendationStrategy {
    List<String> recommend(String studentID);
}

// 2. Các lớp chiến lược cụ thể
class InterestBasedStrategy implements IRecommendationStrategy {
    public List<String> recommend(String studentID) {
        return Arrays.asList("Sự kiện AI & Robotics", "Hội thảo Khởi nghiệp");
    }
}

class HistoryBasedStrategy implements IRecommendationStrategy {
    public List<String> recommend(String studentID) {
        return Arrays.asList("Lớp học kỹ năng mềm", "Tọa đàm hướng nghiệp");
    }
}

class TrendingStrategy implements IRecommendationStrategy {
    public List<String> recommend(String studentID) {
        return Arrays.asList("Lễ hội âm nhạc IUH", "Ngày hội việc làm quốc tế");
    }
}

// 3. Lớp Context quản lý việc sử dụng chiến lược
class RecommendationSystem {
    private IRecommendationStrategy strategy;

    // Cho phép thay đổi chiến lược lúc chạy
    public void setStrategy(IRecommendationStrategy strategy) {
        this.strategy = strategy;
    }

    public void showRecommendations(String studentID) {
        if (strategy == null) {
            System.out.println("Vui lòng chọn phương thức gợi ý!");
            return;
        }
        List<String> results = strategy.recommend(studentID);
        System.out.println("Gợi ý cho sinh viên " + studentID + ": " + results);
    }
}

// 4. Chương trình chính demo
public class Main {
    public static void main(String[] args) {
        RecommendationSystem sys = new RecommendationSystem();

        // Gợi ý theo xu hướng
        sys.setStrategy(new TrendingStrategy());
        sys.showRecommendations("SV2026");

        // Đổi sang gợi ý theo sở thích mà không làm gián đoạn hệ thống
        sys.setStrategy(new InterestBasedStrategy());
        sys.showRecommendations("SV2026");
    }
}