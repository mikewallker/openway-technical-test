# Periplus Shopping Cart Test Documentation

Welcome to the QA Test Strategy and Documentation suite for the Periplus Online Shopping Cart functionality. This comprehensive test plan is structured to balance rigorous coverage with immediate readability for engineering stakeholders. Repetitive steps have been abstracted away, and scenarios are tiered logically from critical user paths down to asynchronous edge cases.

---

## Documentation Structure

The documentation is organized into three distinct, logical sections accessible via the navigation tabs:

1. **[1. Test Case Components](components.md):** Defines the formalized structure utilized for our test design, accompanied by an example of a well-constructed bug report scenario.
2. **[2. Core Test Cases](core-tests.md):** Contains full, long-form test cases detailing the critical path user journeys (P0 workflows) such as standard add-to-cart, quantity modification, and item removal.
3. **[3. Extended Coverage Matrix](extended-matrix.md):** A prioritized coverage matrix mapping out over 30 distinct scenarios, including validation rules, boundary edge cases, business constraints, and asynchronous backend synchronization state behaviors.

---

## Test ID Naming Convention

To maintain a scalable and traceable test suite, Test IDs are treated as permanent keys. 

* **Base Scenarios:** Core actions are assigned a base numeric ID (e.g., `TC-CART-002` for standard quantity updates).
* **Variant Scenarios:** Variations or specific edge cases branching off a base scenario share the numeric root but append an alphabetical suffix. For example, the `002x` series all represent "Add to Cart" or "Cart Input" variants:
  * `TC-CART-002B`: Guest user adds to cart
  * `TC-CART-002C`: Add custom quantity from PDP
  * `TC-CART-002D`: Add out-of-stock item (Blocked)

This convention allows engineers to group related functionalities together logically, even if their execution priorities differ.

---
