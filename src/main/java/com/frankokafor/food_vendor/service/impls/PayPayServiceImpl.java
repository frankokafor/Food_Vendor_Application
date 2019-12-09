package com.frankokafor.food_vendor.service.impls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frankokafor.food_vendor.messages.ErrorMessages;
import com.frankokafor.food_vendor.messages.MessageResponses;
import com.frankokafor.food_vendor.models.Order;
import com.frankokafor.food_vendor.repositories.OrderRepository;
import com.frankokafor.food_vendor.request.objects.ComfirmPaymentModel;
import com.frankokafor.food_vendor.request.objects.PaymentRequestModel;
import com.frankokafor.food_vendor.security.AuthenticatedUserFacade;
import com.frankokafor.food_vendor.services.PayPalService;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Service
public class PayPayServiceImpl implements PayPalService {
	@Autowired
	private APIContext context;
	@Autowired
	private AuthenticatedUserFacade userFacade;
	@Autowired
	private OrderRepository orderRepo;

	@Override
	public Map<String, Object> createPayment(PaymentRequestModel model) {
		return createNewPayment(model);
	}

	@Override
	public Map<String, Object> executePayment(ComfirmPaymentModel model) {
		return executeUserPayment(model);
	}

	private Map<String, Object> createNewPayment(PaymentRequestModel model) {
		Map<String, Object> response = new HashMap<String, Object>();

		Amount amount = new Amount();
		amount.setCurrency(model.getCurrency());
		amount.setTotal(String.format("%.2", model.getTotal()));

		Transaction transaction = new Transaction();
		transaction.setDescription(model.getDescription());
		transaction.setAmount(amount);

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod(model.getMethod());

		Payment payment = new Payment();
		payment.setIntent(model.getIntent());
		payment.setPayer(payer);
		payment.setTransactions(transactions);
		RedirectUrls urls = new RedirectUrls();
		urls.setCancelUrl(model.getCancleUri());
		urls.setReturnUrl(model.getSuccessUri());
		payment.setRedirectUrls(urls);
		Payment newPayment;
		try {
			String redirectUrl = "";
			newPayment = payment.create(context);
			if (newPayment != null) {
				List<Links> links = newPayment.getLinks();
				for (Links link : links) {
					if (link.getRel().equals("approval_url")) {
						redirectUrl = link.getHref();
						break;
					}
				}
				response.put(MessageResponses.CODE, MessageResponses.CODE_OK);
				response.put("redirect_url", redirectUrl);
			}
		} catch (PayPalRESTException e) {
			response.put(MessageResponses.CODE, MessageResponses.CODE_ERROR);
			response.put(MessageResponses.MESSAGE, ErrorMessages.INTERNAL_SERVER_ERROR.getErrorMessages());
			return response;
		}
		return response;
	}

	private Map<String, Object> executeUserPayment(ComfirmPaymentModel model) {
		Order order = orderRepo.findById(model.getOrderId()).get();
		Map<String, Object> response = new HashMap<String, Object>();
		Payment payment = new Payment();
		payment.setId(model.getPaymentId());
		PaymentExecution execution = new PaymentExecution();
		execution.setPayerId(userFacade.getUser().getUsername());
		try {
			Payment newPayment = payment.execute(context, execution);
			if (newPayment != null) {
				order.setPaymentStatus(true);
				orderRepo.save(order);
				response.put(MessageResponses.CODE, MessageResponses.CODE_OK);
				response.put("payment", newPayment);
			}
		} catch (PayPalRESTException e) {
			response.put(MessageResponses.CODE, MessageResponses.CODE_ERROR);
			response.put(MessageResponses.MESSAGE, ErrorMessages.INTERNAL_SERVER_ERROR.getErrorMessages());
			return response;
		}
		return response;
	}

}
