document.addEventListener("DOMContentLoaded", function () {
    const path = window.location.pathname;
    const currentLang = document.documentElement.lang || 'vi';
    const isEnglish = currentLang.startsWith('en');

    // Material theme injects <base href="..."> pointing to the site root.
    // This lets us derive the base path regardless of GitHub Pages sub-directory.
    // e.g., on GitHub Pages: basePath = "/java-docs/"
    //        on local dev:    basePath = "/"
    const basePath = new URL(document.baseURI).pathname;

    let viUrl, enUrl;
    if (isEnglish) {
        // /java-docs/en/page/ → /java-docs/page/
        viUrl = path.replace(/\/en(\/|$)/, '$1') || basePath;
        enUrl = path;
    } else {
        viUrl = path;
        // /java-docs/page/ → /java-docs/en/page/
        const rel = path.startsWith(basePath) ? path.slice(basePath.length) : path.slice(1);
        enUrl = basePath + 'en/' + rel;
    }

    const btn = document.createElement("a");
    btn.href = isEnglish ? viUrl : enUrl;
    btn.className = "lang-btn active";
    btn.textContent = isEnglish ? "EN" : "VI";
    btn.title = isEnglish ? "Switch to Tiếng Việt" : "Switch to English";

    const switcher = document.createElement("div");
    switcher.className = "lang-switcher";
    switcher.appendChild(btn);

    const headerInner = document.querySelector(".md-header__inner");
    const searchBtn = document.querySelector(".md-header__button[for='__search']");

    if (headerInner && searchBtn) {
        headerInner.insertBefore(switcher, searchBtn);
    }
});