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

function openEditModal(btnElement, prefix) {
    const modalId = `edit${prefix}Modal`;
    const editModal = document.getElementById(modalId);
    if (!editModal) {
        console.error(`Không tìm thấy Modal với ID: ${modalId}`);
        return;
    }
    Array.from(btnElement.attributes).forEach(attr => {
        if (attr.name.startsWith('data-')) {
            const fieldName = attr.name.replace('data-', '');
            const formattedFieldName = fieldName.charAt(0).toUpperCase() + fieldName.slice(1);

            const targetInputId = `edit${prefix}${formattedFieldName}`;
            const inputField = document.getElementById(targetInputId);

            if (inputField) {
                if (inputField.type === 'checkbox') {
                    inputField.checked = (attr.value === 'true');
                } else {
                    inputField.value = attr.value;
                }
            }
        }
    });
    const nameValue = btnElement.getAttribute('data-name');
    if (nameValue) {
        const modalTitle = editModal.querySelector('.modal-title');
        if (modalTitle) {
            modalTitle.textContent = `Chỉnh sửa: ${nameValue}`;
        }
    }
    const bsModal = bootstrap.Modal.getOrCreateInstance(editModal);
    bsModal.show();
}

function deleteDetail(url) {
    if (confirm("Xác nhận xoá?") === false)
        return;

    fetch(url, {
        method: "delete"
    }).then(res => {
        if (res.status === 204)
            location.reload();
        else
            alert("Xóa thất bại!");
    });
}

function deleteMulti(url, prefix) {
    const listCheckBox = document.getElementsByClassName(`action-select-${prefix}`);
    const listElementNeedDelete = [];
    Array.from(listCheckBox).forEach(checkBox => {
        if (checkBox.checked === false)
            return;
        const service = {};
        Array.from(checkBox.attributes).forEach(attr => {
            if (attr.name.startsWith('data-') === false)
                return;
            service[attr.name.replace('data-', '')] = attr.value;
        })
        listElementNeedDelete.push(service);
    })

    if (listElementNeedDelete.length === 0) {
        alert("Không có cái nào để xoá!");
        return;
    }

    if (confirm("Chắc chắn xoá không?") === false)
        return;

    fetch(url, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },

        body: JSON.stringify(listElementNeedDelete)
    }).then(res => {
        if (res.status === 204)
            location.reload();
        else
            alert("Xóa thất bại!");
    });
}