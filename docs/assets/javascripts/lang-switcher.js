(function () {
    function getSiteBase() {
        // <base> tag present when navigation.instant is OFF — most reliable source
        var baseEl = document.querySelector('base');
        if (baseEl) {
            try { return new URL(baseEl.href).pathname; } catch (e) {}
        }
        // navigation.instant removes <base>: fall back to hostname-based detection.
        // GitHub Pages project sites: hostname = "user.github.io", first path segment = repo name.
        if (location.hostname.endsWith('.github.io')) {
            var seg = location.pathname.split('/')[1];
            return seg ? '/' + seg + '/' : '/';
        }
        return '/';
    }

    function initLangSwitcher() {
        var path = window.location.pathname;

        // Detect language from the URL — reliable regardless of theme lang attribute.
        // mkdocs-static-i18n always places EN pages under an /en/ path segment.
        var isEnglish = /\/en(\/|$)/.test(path);

        var basePath = getSiteBase();
        var targetUrl;

        if (isEnglish) {
            // /base/en/page/ → /base/page/
            targetUrl = path.replace(/\/en(\/|$)/, '$1') || basePath;
        } else {
            // /base/page/ → /base/en/page/
            var rel = path.startsWith(basePath) ? path.slice(basePath.length) : path.slice(1);
            targetUrl = basePath + 'en/' + rel;
        }

        // Remove any existing switcher before reinserting (navigation.instant re-runs this)
        document.querySelectorAll('.lang-switcher').forEach(function (el) { el.remove(); });

        var btn = document.createElement('a');
        btn.href = targetUrl;
        btn.className = 'lang-btn active';
        btn.textContent = isEnglish ? 'EN' : 'VI';
        btn.title = isEnglish ? 'Switch to Tiếng Việt' : 'Switch to English';

        var switcher = document.createElement('div');
        switcher.className = 'lang-switcher';
        switcher.appendChild(btn);

        var headerInner = document.querySelector('.md-header__inner');
        var searchBtn = document.querySelector('.md-header__button[for="__search"]');

        if (headerInner && searchBtn) {
            headerInner.insertBefore(switcher, searchBtn);
        }
    }

    // Subscribe to Material's document$ observable (fires on every navigation.instant page swap).
    // Falls back to DOMContentLoaded for non-instant builds.
    if (typeof document$ !== 'undefined') {
        document$.subscribe(initLangSwitcher);
    } else {
        document.addEventListener('DOMContentLoaded', initLangSwitcher);
    }
}());