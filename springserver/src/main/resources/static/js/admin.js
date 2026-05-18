function selectAllCheckbox(selectAllId, itemClass) {
    const master = document.getElementById(selectAllId);
    const items = document.getElementsByClassName(itemClass);

    Array.from(items).forEach(cb => {
        cb.checked = master.checked;
    });
}


function resetSelectAllCheckBox(selectAllId, itemClass) {
    const master = document.getElementById(selectAllId);
    const items = document.getElementsByClassName(itemClass);

    const allChecked = Array.from(items).every(cb => cb.checked);

    master.checked = allChecked;
}


function openEditServiceModal(btnElement) {
    if (!btnElement) return;

    const id = btnElement.getAttribute('data-id');
    const name = btnElement.getAttribute('data-name');
    const price = btnElement.getAttribute('data-price');
    const active = btnElement.getAttribute('data-active') === 'true';

    const inputId = document.getElementById('editServiceId');
    const inputName = document.getElementById('editServiceName');
    const inputPrice = document.getElementById('editServicePrice');
    const checkboxActive = document.getElementById('editActiveCheckbox');

    if (inputId) inputId.value = id;
    if (inputName) inputName.value = name;
    if (inputPrice) inputPrice.value = price;
    if (checkboxActive) checkboxActive.checked = active;

    const editModal = document.getElementById('editServiceModal');
    if (editModal) {
        const modalTitle = editModal.querySelector('.modal-title');
        if (modalTitle) {
            modalTitle.textContent = `Sửa dịch vụ: ${name}`;
        }
    }

    const bsModal = bootstrap.Modal.getOrCreateInstance(editModal);
    bsModal.show();
}

function openEditCustomerModal(btnElement) {
    if (!btnElement) return;

    const id = btnElement.getAttribute('data-id');
    const name = btnElement.getAttribute('data-name');
    const active = btnElement.getAttribute('data-active') === 'true';

    const inputId = document.getElementById('editCustomerId');
    const inputName = document.getElementById('editCustomerName');
    const checkboxActive = document.getElementById('editActiveCheckbox');

    if (inputId) inputId.value = id;
    if (inputName) inputName.value = name;
    if (checkboxActive) checkboxActive.checked = active;

    const editModal = document.getElementById('editCustomerModal');
    if (editModal) {
        const modalTitle = editModal.querySelector('.modal-title');
        if (modalTitle) {
            modalTitle.textContent = `Sửa thông tin khách hàng: ${name}`;
        }
    }

    const bsModal = bootstrap.Modal.getOrCreateInstance(editModal);
    bsModal.show();
}