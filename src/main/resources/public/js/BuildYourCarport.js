function getCheckedValue(name) {
    const checkedInput = document.querySelector(`input[name="${name}"]:checked`);
    return checkedInput ? checkedInput.value : "";
}

async function updatePreview() {
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

    const previewImage = document.getElementById("carportPreviewImage");

    if (type === "Enkelt carport" && roofType === "Fladt tag" && !hasShed) {
        previewImage.src = "/images/enkeltCarportUdenSkurOgUdenRejsning.png";
    } else if (type === "Enkelt carport" && roofType === "Fladt tag" && hasShed) {
        previewImage.src = "/images/enkeltCarportMedSkurOgUdenRejsning.png";
    } else if (type === "Enkelt carport" && roofType === "Rejsning" && !hasShed) {
        previewImage.src = "/images/enkeltCarportUdenSkurMedRejsning.png";
    } else if (type === "Enkelt carport" && roofType === "Rejsning" && hasShed) {
        previewImage.src = "/images/enkeltCarportMedSkurOgMedRejsning.png";
    } else if (type === "Dobbelt carport" && roofType === "Fladt tag" && !hasShed) {
        previewImage.src = "/images/dobbeltCarportUdenSkurOgUdenRejsning.png";
    } else if (type === "Dobbelt carport" && roofType === "Fladt tag" && hasShed) {
        previewImage.src = "/images/dobbeltCarportMedSkurUdenRejsning.png";
    } else if (type === "Dobbelt carport" && roofType === "Rejsning" && !hasShed) {
        previewImage.src = "/images/dobbeltCarportUdenSkurOgRejsning.png";
    } else if (type === "Dobbelt carport" && roofType === "Rejsning" && hasShed) {
        previewImage.src = "/images/dobbeltCarportMedSkurOgRejsning.png";
    }

    const form = document.getElementById("carportRequestForm");
    const formData = new FormData(form);

    try {
        const response = await fetch("/calculate-preview-price", {
            method: "POST",
            body: formData
        });
        const price = await response.text();

        console.log(price);

        if (price === "error") {
            document.getElementById("summaryPrice").textContent = "Udfyld alle felter";
        } else {
            document.getElementById("summaryPrice").textContent = price + " DKK";
        }
    } catch (error) {
        console.log(error);
        document.getElementById("summaryPrice").textContent = "-";
    }
}