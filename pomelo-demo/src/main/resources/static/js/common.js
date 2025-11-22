// 通用JavaScript工具函数

/**
 * 显示通知消息
 * @param {string} message - 消息内容
 * @param {string} type - 消息类型: success, error, warning, info
 * @param {number} duration - 显示时长(毫秒)，默认3000
 */
function showNotification(message, type = 'info', duration = 3000) {
    // 移除已存在的通知
    const existingNotification = document.querySelector('.notification');
    if (existingNotification) {
        existingNotification.remove();
    }

    // 创建新通知
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    
    document.body.appendChild(notification);

    // 自动移除
    setTimeout(() => {
        if (notification.parentNode) {
            notification.remove();
        }
    }, duration);
}

/**
 * 显示确认对话框
 * @param {string} message - 确认消息
 * @param {function} onConfirm - 确认回调
 * @param {function} onCancel - 取消回调
 */
function showConfirm(message, onConfirm, onCancel) {
    if (confirm(message)) {
        if (onConfirm) onConfirm();
    } else {
        if (onCancel) onCancel();
    }
}

/**
 * 格式化日期
 * @param {Date|string} date - 日期对象或字符串
 * @param {string} format - 格式化模式
 * @returns {string} 格式化后的日期字符串
 */
function formatDate(date, format = 'YYYY-MM-DD HH:mm:ss') {
    if (!date) return '-';
    
    const d = new Date(date);
    if (isNaN(d.getTime())) return '-';
    
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    const hours = String(d.getHours()).padStart(2, '0');
    const minutes = String(d.getMinutes()).padStart(2, '0');
    const seconds = String(d.getSeconds()).padStart(2, '0');
    
    return format
        .replace('YYYY', year)
        .replace('MM', month)
        .replace('DD', day)
        .replace('HH', hours)
        .replace('mm', minutes)
        .replace('ss', seconds);
}

/**
 * 防抖函数
 * @param {function} func - 要防抖的函数
 * @param {number} wait - 等待时间(毫秒)
 * @returns {function} 防抖后的函数
 */
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

/**
 * 节流函数
 * @param {function} func - 要节流的函数
 * @param {number} limit - 限制时间(毫秒)
 * @returns {function} 节流后的函数
 */
function throttle(func, limit) {
    let inThrottle;
    return function() {
        const args = arguments;
        const context = this;
        if (!inThrottle) {
            func.apply(context, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    };
}

/**
 * 深拷贝对象
 * @param {any} obj - 要拷贝的对象
 * @returns {any} 拷贝后的对象
 */
function deepClone(obj) {
    if (obj === null || typeof obj !== 'object') return obj;
    if (obj instanceof Date) return new Date(obj.getTime());
    if (obj instanceof Array) return obj.map(item => deepClone(item));
    if (typeof obj === 'object') {
        const clonedObj = {};
        for (const key in obj) {
            if (obj.hasOwnProperty(key)) {
                clonedObj[key] = deepClone(obj[key]);
            }
        }
        return clonedObj;
    }
}

/**
 * 获取URL参数
 * @param {string} name - 参数名
 * @returns {string|null} 参数值
 */
function getUrlParameter(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}

/**
 * 设置URL参数
 * @param {string} name - 参数名
 * @param {string} value - 参数值
 */
function setUrlParameter(name, value) {
    const url = new URL(window.location);
    url.searchParams.set(name, value);
    window.history.pushState({}, '', url);
}

/**
 * 本地存储工具
 */
const Storage = {
    /**
     * 设置本地存储
     * @param {string} key - 键
     * @param {any} value - 值
     */
    set(key, value) {
        try {
            localStorage.setItem(key, JSON.stringify(value));
        } catch (e) {
            console.error('设置本地存储失败:', e);
        }
    },

    /**
     * 获取本地存储
     * @param {string} key - 键
     * @param {any} defaultValue - 默认值
     * @returns {any} 存储的值
     */
    get(key, defaultValue = null) {
        try {
            const item = localStorage.getItem(key);
            return item ? JSON.parse(item) : defaultValue;
        } catch (e) {
            console.error('获取本地存储失败:', e);
            return defaultValue;
        }
    },

    /**
     * 移除本地存储
     * @param {string} key - 键
     */
    remove(key) {
        try {
            localStorage.removeItem(key);
        } catch (e) {
            console.error('移除本地存储失败:', e);
        }
    },

    /**
     * 清空本地存储
     */
    clear() {
        try {
            localStorage.clear();
        } catch (e) {
            console.error('清空本地存储失败:', e);
        }
    }
};

/**
 * HTTP请求工具
 */
const Http = {
    /**
     * GET请求
     * @param {string} url - 请求URL
     * @param {object} options - 请求选项
     * @returns {Promise} 请求Promise
     */
    async get(url, options = {}) {
        return this.request(url, { ...options, method: 'GET' });
    },

    /**
     * POST请求
     * @param {string} url - 请求URL
     * @param {any} data - 请求数据
     * @param {object} options - 请求选项
     * @returns {Promise} 请求Promise
     */
    async post(url, data, options = {}) {
        return this.request(url, {
            ...options,
            method: 'POST',
            body: JSON.stringify(data),
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            }
        });
    },

    /**
     * PUT请求
     * @param {string} url - 请求URL
     * @param {any} data - 请求数据
     * @param {object} options - 请求选项
     * @returns {Promise} 请求Promise
     */
    async put(url, data, options = {}) {
        return this.request(url, {
            ...options,
            method: 'PUT',
            body: JSON.stringify(data),
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            }
        });
    },

    /**
     * DELETE请求
     * @param {string} url - 请求URL
     * @param {object} options - 请求选项
     * @returns {Promise} 请求Promise
     */
    async delete(url, options = {}) {
        return this.request(url, { ...options, method: 'DELETE' });
    },

    /**
     * 通用请求方法
     * @param {string} url - 请求URL
     * @param {object} options - 请求选项
     * @returns {Promise} 请求Promise
     */
    async request(url, options = {}) {
        try {
            const response = await fetch(url, options);
            
            if (!response.ok) {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }

            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('application/json')) {
                return await response.json();
            } else {
                return await response.text();
            }
        } catch (error) {
            console.error('请求失败:', error);
            throw error;
        }
    }
};

/**
 * 表单验证工具
 */
const Validator = {
    /**
     * 验证必填字段
     * @param {string} value - 字段值
     * @returns {boolean} 是否有效
     */
    required(value) {
        return value !== null && value !== undefined && String(value).trim() !== '';
    },

    /**
     * 验证邮箱格式
     * @param {string} email - 邮箱地址
     * @returns {boolean} 是否有效
     */
    email(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    },

    /**
     * 验证URL格式
     * @param {string} url - URL地址
     * @returns {boolean} 是否有效
     */
    url(url) {
        try {
            new URL(url);
            return true;
        } catch {
            return false;
        }
    },

    /**
     * 验证数字
     * @param {any} value - 值
     * @returns {boolean} 是否为数字
     */
    number(value) {
        return !isNaN(value) && !isNaN(parseFloat(value));
    },

    /**
     * 验证整数
     * @param {any} value - 值
     * @returns {boolean} 是否为整数
     */
    integer(value) {
        return Number.isInteger(Number(value));
    },

    /**
     * 验证最小长度
     * @param {string} value - 字符串值
     * @param {number} min - 最小长度
     * @returns {boolean} 是否有效
     */
    minLength(value, min) {
        return String(value).length >= min;
    },

    /**
     * 验证最大长度
     * @param {string} value - 字符串值
     * @param {number} max - 最大长度
     * @returns {boolean} 是否有效
     */
    maxLength(value, max) {
        return String(value).length <= max;
    }
};

/**
 * 页面加载完成后执行
 */
document.addEventListener('DOMContentLoaded', function() {
    // 为所有带有data-tooltip属性的元素添加工具提示
    document.querySelectorAll('[data-tooltip]').forEach(element => {
        element.addEventListener('mouseenter', function() {
            const tooltip = document.createElement('div');
            tooltip.className = 'tooltip-popup';
            tooltip.textContent = this.getAttribute('data-tooltip');
            tooltip.style.cssText = `
                position: absolute;
                background: #333;
                color: white;
                padding: 5px 10px;
                border-radius: 4px;
                font-size: 12px;
                z-index: 1000;
                pointer-events: none;
            `;
            document.body.appendChild(tooltip);

            const rect = this.getBoundingClientRect();
            tooltip.style.left = rect.left + 'px';
            tooltip.style.top = (rect.top - tooltip.offsetHeight - 5) + 'px';

            this._tooltip = tooltip;
        });

        element.addEventListener('mouseleave', function() {
            if (this._tooltip) {
                this._tooltip.remove();
                this._tooltip = null;
            }
        });
    });

    // 为所有表单添加基本验证
    document.querySelectorAll('form').forEach(form => {
        form.addEventListener('submit', function(e) {
            const requiredFields = this.querySelectorAll('[required]');
            let isValid = true;

            requiredFields.forEach(field => {
                if (!Validator.required(field.value)) {
                    field.style.borderColor = '#f56565';
                    isValid = false;
                } else {
                    field.style.borderColor = '#d1d5db';
                }
            });

            if (!isValid) {
                e.preventDefault();
                showNotification('请填写所有必填字段', 'error');
            }
        });
    });
});

// 导出到全局作用域
window.showNotification = showNotification;
window.showConfirm = showConfirm;
window.formatDate = formatDate;
window.debounce = debounce;
window.throttle = throttle;
window.deepClone = deepClone;
window.getUrlParameter = getUrlParameter;
window.setUrlParameter = setUrlParameter;
window.Storage = Storage;
window.Http = Http;
window.Validator = Validator;