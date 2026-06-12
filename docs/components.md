# Part 1: Components of a Test Case

A standard test case should include:

- **Test Case ID:** A unique identifier (e.g., TC-CART-001).
- **Title:** A brief description of what is being tested.
- **Preconditions:** State of the application required before testing.
- **Test Steps:** Clear, actionable steps to execute the test.
- **Expected Result:** The exact behavior expected after executing the steps.
- **Actual Result:** (Filled during execution) What actually happened.
- **Status:** Pass/Fail.

Example:

# Test Case ID: TC-AUTH-001

## Title
Verify successful user login with valid credentials.

## Preconditions
- The user possesses a registered account with valid credentials.
- The user is navigated to the application's Login page.

## Test Steps
1. Enter the valid registered email address into the **Email** input field.
2. Enter the corresponding valid password into the **Password** input field.
3. Click the **Sign In** button.

## Expected Result
- The system successfully authenticates the credentials.
- The user is redirected to the authenticated Dashboard/Homepage.
- A **Welcome** confirmation is displayed in the UI.

## Actual Result
- The credentials were accepted after clicking **Sign In**.
- The user was **not redirected** to the Dashboard/Homepage and remained on the Login page.
- No **Welcome** confirmation message was displayed.
- A browser console error (`500 Internal Server Error`) was observed during the login request.

## Status
**Failed**
