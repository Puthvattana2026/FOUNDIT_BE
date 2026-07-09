package backend.services.payment;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import backend.dtos.payment.ManualPaymentSubmitResponse;
import backend.dtos.payment.PaymentTransactionResponse;
import backend.models.freelancer.setting.Setting;
import backend.models.payment.PaymentTransaction;

public interface PaymentService {
    ManualPaymentSubmitResponse submitManualPayment(Authentication auth, Long projectId, String reference, MultipartFile proofFile);
    PaymentTransaction confirmManualPayment(Authentication auth, String tranId);
    PaymentTransaction getFreelancerPaymentTransaction(Authentication auth, String tranId);
    Setting getSellerPaymentSetting(Authentication auth, Long projectId);
    Setting getSellerPaymentSettingByGig(Authentication auth, Long gigId);
    PaymentTransaction checkTransaction(String tranId);
    List<PaymentTransaction> getClientTransactions(Authentication auth);
    List<PaymentTransaction> getFreelancerTransactions(Authentication auth);
    List<PaymentTransactionResponse> getClientTransactionResponses(Authentication auth);
    List<PaymentTransactionResponse> getFreelancerTransactionResponses(Authentication auth);
}
