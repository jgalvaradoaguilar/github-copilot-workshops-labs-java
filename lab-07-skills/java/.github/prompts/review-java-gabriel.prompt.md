---
description: "Perform a structured code review for Java code"
name: "Review Java Gabriel"
agent: "agent"
---

Perform a technical review of the following Java code.

☕ **Code to review**:
${input:code:Paste your Java code here}

⚖️ **Focus areas**:
${input:criteria:readability, performance, security, maintainability, testing}

Please respond with:
- Findings grouped by each selected area
- Potential risks and how to fix them
- Recommended refactors or Java best practice improvements
- Quick wins with priority levels (high/medium/low)