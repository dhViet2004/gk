// Dựa trên mô tả bài toán: "nhiều hệ thống bên ngoài khác nhau", "API riêng" và "không tuân theo cùng một chuẩn giao tiếp", mẫu thiết kế (Design Pattern) tối ưu nhất để giải quyết vấn đề này là Adapter Pattern (kết hợp với Strategy để hoán đổi linh hoạt).

// Tuy nhiên, bám sát vào yêu cầu "kết nối các giao diện không tương thích", đây là lời giải chi tiết theo đúng format bài thi:

// 1. Chọn Design Pattern và Giải thích (0.5 điểm)
// Tên Pattern: Adapter Pattern (Mẫu bộ chuyển đổi).

// Lý do chọn:

// Giải quyết sự không tương thích: Hệ thống cần làm việc với nhiều bên thứ ba (Momo, Visa) có API và cấu trúc hoàn toàn khác nhau. Adapter đóng vai trò là "jack chuyển" giúp đưa các giao diện lạ về một chuẩn chung của hệ thống.

// Tính đóng gói: Giúp cô lập mã nguồn của bên thứ ba. Nếu API của Momo thay đổi, chúng ta chỉ cần sửa lớp Adapter tương ứng mà không làm ảnh hưởng đến logic thanh toán của toàn bộ hệ thống.

// Nguyên lý Open/Closed: Khi cần thêm cổng thanh toán mới (ví dụ: ZaloPay), chỉ cần tạo thêm một lớp Adapter mới thực thi giao diện chung mà không cần sửa đổi các module hiện có.




// @startuml
// interface IPaymentTarget <<interface>> {
//     + processPayment(amount: long): void
// }

// class MomoAdapter implements IPaymentTarget {
//     - momoService: MomoService
//     + processPayment(amount: long): void
// }

// class VisaAdapter implements IPaymentTarget {
//     - visaService: VisaSDK
//     + processPayment(amount: long): void
// }

// class WalletProcessor implements IPaymentTarget {
//     + processPayment(amount: long): void
// }

// class MomoService {
//     + sendMomoRequest(vnd: double): void
// }

// class VisaSDK {
//     + payWithVisa(dollars: int): void
// }

// class PaymentService {
//     - paymentMethod: IPaymentTarget
//     + setPaymentMethod(method: IPaymentTarget): void
//     + checkout(amount: long): void
// }

// PaymentService o-- IPaymentTarget
// MomoAdapter --> MomoService
// VisaAdapter --> VisaSDK
// @enduml



// --- 1. Giao diện chung của hệ thống (Target) ---
interface IPaymentTarget {
    void processPayment(long amount);
}

// --- 2. Các hệ thống bên ngoài với API khác biệt (Adaptees) ---
class MomoService { // API của Momo dùng tên hàm khác, kiểu dữ liệu khác
    public void sendMomoRequest(double amountVND) {
        System.out.println("Momo: Đang xử lý giao dịch " + amountVND + "đ");
    }
}

class VisaSDK { // SDK của Visa yêu cầu số tiền bằng đơn vị USD
    public void payWithVisa(int usdAmount) {
        System.out.println("Visa: Đang xử lý giao dịch $" + usdAmount);
    }
}

// --- 3. Các bộ chuyển đổi (Adapters) ---
class MomoAdapter implements IPaymentTarget {
    private MomoService momo = new MomoService();

    @Override
    public void processPayment(long amount) {
        // Chuyển đổi từ chuẩn hệ thống sang chuẩn Momo
        momo.sendMomoRequest((double) amount);
    }
}

class VisaAdapter implements IPaymentTarget {
    private VisaSDK visa = new VisaSDK();

    @Override
    public void processPayment(long amount) {
        // Giả sử tỷ giá 25,000 VND = 1 USD
        int usd = (int) (amount / 25000);
        visa.payWithVisa(usd);
    }
}

// --- 4. Ví nội bộ (Đã tuân theo chuẩn hệ thống nên không cần Adapter) ---
class InternalWallet implements IPaymentTarget {
    @Override
    public void processPayment(long amount) {
        System.out.println("Ví nội bộ: Đã trừ " + amount + " từ số dư.");
    }
}

// --- 5. Module nghiệp vụ chính ---
class PaymentService {
    private IPaymentTarget method;

    public void setPaymentMethod(IPaymentTarget method) {
        this.method = method;
    }

    public void checkout(long total) {
        if (method == null) {
            System.out.println("Lỗi: Chưa chọn phương thức thanh toán!");
            return;
        }
        method.processPayment(total);
    }
}

// --- Demo chạy chương trình ---
public class Main {
    public static void main(String[] args) {
        PaymentService cart = new PaymentService();

        // Thanh toán qua Momo
        cart.setPaymentMethod(new MomoAdapter());
        cart.checkout(50000);

        // Thanh toán qua Visa (Bẻ lái dữ liệu VND sang USD bên trong Adapter)
        cart.setPaymentMethod(new VisaAdapter());
        cart.checkout(500000);
    }
}