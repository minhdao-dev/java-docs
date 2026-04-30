# NMD Java Docs

**Vietnamese Java backend learning path — from JDK setup to microservices.**

[![Live Site](https://img.shields.io/badge/Live%20Site-minhdao--dev.github.io%2Fjava--docs-blue?style=flat-square)](https://minhdao-dev.github.io/java-docs/)
[![Deploy](https://github.com/minhdao-dev/java-docs/actions/workflows/deploy.yml/badge.svg)](https://github.com/minhdao-dev/java-docs/actions/workflows/deploy.yml)
[![Lint](https://github.com/minhdao-dev/java-docs/actions/workflows/lint.yml/badge.svg)](https://github.com/minhdao-dev/java-docs/actions/workflows/lint.yml)

---

## About

A structured, bilingual documentation site for Vietnamese developers learning Java backend
development. Written in Vietnamese first, with a parallel English translation — not a
Google-translated afterthought.

**Why this exists:**

Most Java tutorials are written in English, assume a CS degree, or drown beginners in theory
before touching anything practical. This site is written for Vietnamese junior-to-mid developers
who already know basic programming and want a clear, opinionated path from zero to production-ready
Java backend skills.

**What makes it different:**
- Vietnamese is the source language — every lesson is written in Vietnamese first
- Progressive roadmap, not a loose reference — phases build on each other
- Diagrams included for every non-obvious concept (18 custom SVGs so far)
- Real code examples in `examples/` are linted and verified by CI

---

## Live Site

| Language | URL |
|----------|-----|
| Vietnamese (default) | <https://minhdao-dev.github.io/java-docs/> |
| English | <https://minhdao-dev.github.io/java-docs/en/> |

<!-- Add screenshots here once the site design stabilises -->
<!-- Vietnamese: docs/assets/screenshots/vi-preview.png -->
<!-- English:    docs/assets/screenshots/en-preview.png -->

---

## Roadmap

| # | Phase | Status |
|---|-------|--------|
| 00 | Setup & Mindset | ✅ Done |
| 01 | Java Fundamentals | 🚧 18 / 19 lessons |
| 02 | Java Core Advanced — Collections, Generics, Lambda, Stream API, Concurrency | ⬜ Planned |
| 03 | Build Tools & Testing — Maven, JUnit 5, Mockito, TDD | ⬜ Planned |
| 04 | SQL & Databases — PostgreSQL, JDBC, Transactions | ⬜ Planned |
| 05 | Spring Boot Core — REST API, Spring Data JPA, Actuator | ⬜ Planned |
| 06 | Spring Security & JWT — Auth, OAuth2 | ⬜ Planned |
| 07 | Redis & Caching | ⬜ Planned |
| 08 | Docker & CI/CD | ⬜ Planned |
| 09 | Microservices & Kafka | ⬜ Planned |
| 10 | DSA & Interview Prep | ⬜ Planned |

ETAs are rough targets, not commitments. Each phase is published incrementally as lessons are
finished — you can track progress in the nav or via commits.

---

## Tech Stack

| Tool | Purpose |
|------|---------|
| [MkDocs Material](https://squidfunk.github.io/mkdocs-material/) | Static site generator and theme |
| [mkdocs-static-i18n](https://github.com/ultrabug/mkdocs-static-i18n) | Bilingual docs via `.vi.md` / `.en.md` file suffixes |
| [GitHub Pages](https://pages.github.com/) | Hosting |
| [GitHub Actions](https://github.com/features/actions) | Build, lint, deploy, and example verification |

CI runs three workflows on every push: `deploy.yml` builds and publishes the site, `lint.yml`
checks Markdown style and spelling, and `verify-examples.yml` compiles the code in `examples/`.

---

## Local Development

**Requirements:** Python 3.9+

```bash
git clone https://github.com/minhdao-dev/java-docs.git
cd java-docs
pip install -r requirements.txt
mkdocs serve
```

The dev server starts at `http://127.0.0.1:8000` and hot-reloads on file changes.

To build the static site locally:

```bash
mkdocs build
# output → site/  (git-ignored, do not commit)
```

The `requirements.txt` pins three packages: `mkdocs`, `mkdocs-material[imaging]`, and
`mkdocs-static-i18n`. The `[imaging]` extra requires `cairo` and `pngquant` for social card
generation — omit it if you only need the docs themselves.

---

## Contributing

Issues are welcome for typos, broken links, unclear explanations, or missing content. Include the
phase number and lesson name in the issue title (e.g., `Phase 01 / Variables — example is wrong`).

Pull requests are welcome with one condition: each PR must cover a single lesson or a single fix.
Mixed-scope PRs will be asked to split. Match the existing tone — short paragraphs, concrete
examples, no padding. If you are adding a Vietnamese lesson, the English translation is optional
but appreciated.

---

## License

Documentation content (`/docs`, `/examples`) — [CC BY 4.0](LICENSE)

MkDocs configuration and build scripts — MIT

---

## Author

[Nguyen Minh Dao](https://github.com/minhdao-dev)

---

## GitHub Settings to Update Manually

**Repo description** (one sentence, &lt;100 chars):

```
Vietnamese Java backend learning path — from JDK setup to microservices.
```

**Topics** (8–10 GitHub topics):

```
java  java-backend  mkdocs  mkdocs-material  github-pages  vietnamese
learning  spring-boot  roadmap  documentation
```

**Website URL** (repo homepage field):

```
https://minhdao-dev.github.io/java-docs/
```
