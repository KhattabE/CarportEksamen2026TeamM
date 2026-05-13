function getCheckedValue(name) {
    const checkedInput = document.querySelector(`input[name="${name}"]:checked`);
    return checkedInput ? checkedInput.value : "";
}

function updatePreview() {
    const length = document.getElementById("length_cm").value || "-";
    const width = document.getElementById("width_cm").value || "-";
    const height = document.getElementById("height_cm").value || "-";
    const material = document.getElementById("material").value;
    const roofType = document.getElementById("roof_type").value;
    const type = getCheckedValue("carport_type");
    const hasShed = getCheckedValue("has_shed") === "true";

    document.getElementById("summaryType").textContent = type;
    document.getElementById("summaryMeasurements").textContent = `${length} * ${width} * ${height} cm`;
    document.getElementById("summaryMaterial").textContent = material;
    document.getElementById("summaryRoofType").textContent = roofType;
    document.getElementById("summaryShed").textContent = hasShed ? "Ja" : "Nej";

    document.getElementById("summaryPrice").textContent = "-";
}