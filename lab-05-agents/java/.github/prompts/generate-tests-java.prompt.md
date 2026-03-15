---
description: "Generate JUnit unit tests for a given Java method"
name: "Generate Tests Java"
agent: "agent"
---

Write a JUnit test suite for the following Java code.

🧩 **Code under test**:
${input:code:Paste the Java method or class here}

🧠 **Test strategy**:
${input:matrix:Describe the edge cases, invalid inputs, and expected failures}

Requirements:
- Use JUnit 5 conventions
- Cover success, edge, and failure scenarios
- Use clear and descriptive test names
- Include minimal setup and teardown (@BeforeEach, @AfterEach if needed)
- Highlight any missing test coverage areas