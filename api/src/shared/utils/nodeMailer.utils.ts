import nodemailer from "nodemailer";

const generateCode = (): string => {
    return Math.floor(100000 + Math.random() * 900000).toString();
};

const transporter = nodemailer.createTransport({
    service: "gmail",
    auth: {
        user: process.env.SMTP_USER,
        pass: process.env.SMTP_PASS,
    },
});

const mailOptions = (email: string, code: string) => ({
    from: process.env.SMTP_USER,
    to: email,
    subject: "Confirmação de E-mail - FATEC",
    html: `
      <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px; background-color: #ffffff;">
        <div style="text-align: center; margin-bottom: 20px;">
          <img src="https://fatweb.s3.amazonaws.com/vestibularfatec/assets/img/layout/logotipo-fatec.png" alt="Logo da FATEC" style="max-width: 200px;" />
        </div>
  
        <h2 style="color: #a6192e; text-align: center;">Confirmação de E-mail</h2>
  
        <p style="color: #3d3d3d;">Olá!</p>
        <p style="color: #3d3d3d;">
          Você está recebendo este e-mail porque solicitou a criação de uma conta no sistema de Horas de Atividades Extracurriculares da 
          <strong style="color: #a6192e;">FATEC (Faculdade de Tecnologia do Estado de São Paulo)</strong>.
        </p>
  
        <p style="font-size: 18px; color: #3d3d3d;"><strong>Seu código de confirmação é:</strong></p>
  
        <div style="text-align: center; margin: 20px 0;">
          <span style="font-size: 28px; font-weight: bold; background-color: #a6192e; color: white; padding: 12px 24px; display: inline-block; border-radius: 6px;">
            ${code}
          </span>
        </div>
    
        <p style="color: #3d3d3d;">Este código é pessoal, intransferível e expira em alguns minutos por motivos de segurança.</p>
        <p style="color: #3d3d3d;">Caso você não tenha solicitado este cadastro, por favor ignore esta mensagem.</p>
  
        <br />
  
        <p style="color: #3d3d3d;">Atenciosamente,</p>
        <p style="color: #3d3d3d;">
          <strong>Equipe Técnica da FATEC</strong><br />
          <a href="https://www.fatec.sp.gov.br" target="_blank" style="color: #a6192e; text-decoration: none;">www.fatec.sp.gov.br</a>
        </p>
      </div>
    `
  });
  

export {
    generateCode,
    transporter,
    mailOptions,
};
