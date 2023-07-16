var stripe = Stripe('sk_test_51NTv7IA3PK6TpPfAyf7dhuRd3Yn7JPPEz80X6BqnpJD0N4YYsZPmSwJrxgWOvZz3t32qkxefFtkmKwTEa22vJoSs00pviVmkKY');

var elements = stripe.elements();
var cardElement = elements.create('card');
cardElement.mount('#card-element');

var form = document.getElementById('payment-form');

form.addEventListener('submit', function(event) {
    event.preventDefault();

    stripe.createPaymentMethod({
        type: 'card',
        card: cardElement,
        billing_details: {
            name: document.querySelector('input[name="cardholder-name"]').value
        }
    }).then(function(result) {
        if (result.error) {
            // Handle error
        } else {
            // Send payment method to your backend
            fetch('/charge', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ payment_method_id: result.paymentMethod.id })
            }).then(function(response) {
                return response.json();
            }).then(function(data) {
                // Handle response from your backend
            });
        }
    });
});
