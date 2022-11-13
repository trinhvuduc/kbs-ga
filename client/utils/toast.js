import { toast } from "bulma-toast";

export const toastSuccess = (message) => {
  toast({
    message: message,
    type: "is-success",
    dismissible: true,
    closeOnClick: true,
    pauseOnHover: true,
    duration: 3000,
    animate: { in: "fadeInRight", out: "fadeOutRight" },
  });
}

export const toastError = (message) => {
  toast({
    message: message ?? 'Đã có lỗi xảy ra!',
    type: "is-danger",
    dismissible: true,
    closeOnClick: true,
    pauseOnHover: true,
    duration: 3000,
    animate: { in: "fadeInRight", out: "fadeOutRight" },
  });
}
